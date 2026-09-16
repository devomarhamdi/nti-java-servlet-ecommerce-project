package dao;

import model.Cart;
import model.CartItem;
import model.Order;
import model.OrderItem;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    /**
     * FR-25/FR-26: order history for a customer, most recent first. Item
     * detail is intentionally left out here (see findByIdWithItems) so the
     * list stays a single cheap query.
     */
    public List<Order> findByCustomer(int customerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, customer_id, order_date, total_amount, status " +
                "FROM orders WHERE customer_id = ? ORDER BY order_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        return orders;
    }

    /**
     * FR-38/FR-39: a single order together with its line items, joined to
     * products so each line shows the product name and the price actually
     * charged (order_items.price), not today's catalog price.
     * <p>
     * customerId scopes the lookup to the requesting customer so one
     * customer cannot view another customer's order by guessing an id.
     */
    public Order findByIdWithItems(int orderId, int customerId) throws SQLException {
        String orderSql = "SELECT id, customer_id, order_date, total_amount, status " +
                "FROM orders WHERE id = ? AND customer_id = ?";
        Order order;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(orderSql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                order = mapOrder(rs);
            }
        }

        String itemsSql = "SELECT oi.product_id, p.name AS product_name, oi.quantity, oi.price " +
                "FROM order_items oi JOIN products p ON oi.product_id = p.id " +
                "WHERE oi.order_id = ? ORDER BY oi.id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(itemsSql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setOrderId(orderId);
                    item.setProductId(rs.getInt("product_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getBigDecimal("price"));
                    order.getItems().add(item);
                }
            }
        }
        return order;
    }

    /**
     * FR-11 to FR-19: turns the session cart into a persisted order. Stock is
     * checked and decremented in the same transaction as the order/order_item
     * inserts so a sold-out product can't be oversold by a concurrent
     * checkout, and any failure rolls the whole order back.
     */
    public int createOrder(int customerId, Cart cart) throws SQLException {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot checkout an empty cart");
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                for (CartItem item : cart.getItems()) {
                    decrementStock(conn, item);
                }

                int orderId = insertOrder(conn, customerId, cart.getTotal());

                for (CartItem item : cart.getItems()) {
                    insertOrderItem(conn, orderId, item);
                }

                conn.commit();
                return orderId;
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private void decrementStock(Connection conn, CartItem item) throws SQLException {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getQuantity());
            ps.setInt(2, item.getProduct().getId());
            ps.setInt(3, item.getQuantity());
            if (ps.executeUpdate() == 0) {
                throw new InsufficientStockException(item.getProduct().getName());
            }
        }
    }

    private int insertOrder(Connection conn, int customerId, BigDecimal total) throws SQLException {
        String sql = "INSERT INTO orders (customer_id, total_amount, status) VALUES (?, ?, 'Pending')";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customerId);
            ps.setBigDecimal(2, total);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    private void insertOrderItem(Connection conn, int orderId, CartItem item) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, item.getProduct().getId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getProduct().getPrice());
            ps.executeUpdate();
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setCustomerId(rs.getInt("customer_id"));
        Timestamp orderDate = rs.getTimestamp("order_date");
        if (orderDate != null) {
            order.setOrderDate(orderDate.toLocalDateTime());
        }
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(rs.getString("status"));
        return order;
    }

    /**
     * Raised when a product no longer has enough stock to satisfy a cart
     * item at checkout time.
     */
    public static class InsufficientStockException extends RuntimeException {
        public InsufficientStockException(String productName) {
            super("Not enough stock available for \"" + productName + "\".");
        }
    }
}
