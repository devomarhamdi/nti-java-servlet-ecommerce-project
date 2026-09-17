package dao;

import model.*;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private Connection connection = DBConnection.getConnection();
    private Cart cart;
    private Order order;

    public OrderDAO(Order order) throws SQLException {
        this.order = order;
    }

    public void createOrder(Cart cart, Customer customer)throws SQLException {
        connection.setAutoCommit(false);
        try{
            PreparedStatement pstmtOrder = connection.prepareStatement("INSERT INTO orders (customer_id, total_amount,order_status) VALUES (?, ?, ?)");
            pstmtOrder.setInt(1, customer.getId());
            pstmtOrder.setBigDecimal(2, insertOrderItem(cart,order));
            pstmtOrder.setString(3, OrderStatus.Pending.toString());
            connection.commit();
        }catch (Exception e){
            connection.rollback();
        }
    }

    public BigDecimal insertOrderItem(Cart cart, Order order) throws SQLException {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            BigDecimal price = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO orderitems VALUES (?,?,?,?)");
            pstm.setInt(1, order.getId());
            pstm.setInt(2,item.getProduct().getId());
            pstm.setInt(3,item.getQuantity());
            pstm.setBigDecimal(4,price);
            total.add(price);
        }
        return total;
    }

    public void decrementStock(Cart cart) throws SQLException {
        for (CartItem cartItem : cart.getItems()) {
            PreparedStatement pstm= connection.prepareStatement("UPDATE products SET stock=stock-? WHERE product_id=?");
            pstm.setInt(1, cartItem.getQuantity());
            pstm.setInt(2, cartItem.getProduct().getId());
        }
    }

    public List<Order> getAllOrdersToCustomer(Customer customer) throws SQLException {
        List<Order> orders = new ArrayList<>();
        connection.setAutoCommit(false);
        String query = "SELECT order_date,total_amount,status FROM orders WHERE customer_id="+customer.getId();
        Statement stmt = connection.createStatement();
        try(ResultSet rs = stmt.executeQuery(query)){
            while(rs.next()){
                order.setOrderDate(rs.getDate(1).toLocalDate().atStartOfDay());
                order.setOrderAmount(rs.getDouble(2));
                order.setOrderStatus(OrderStatus.valueOf(rs.getString(3)));
                orders.add(order);
            }
            connection.commit();
        }catch (Exception e){
            connection.rollback();
        }
        return orders;
    }

    public List<OrderAdminDTO> getAllOrdersToAdmin() throws SQLException {
        List<OrderAdminDTO> orders = new ArrayList<>();
        String query = "SELECT * FROM orders " +
                "join order_items on order.id = order_items.order_id" +
                "ORDER BY order_date";
        Statement stmt = connection.createStatement();
        try(ResultSet rs = stmt.executeQuery(query)){
            while(rs.next()){
                ResultSet resultSet = stmt.executeQuery(query);
                int customerId = rs.getInt("customer_id");
                int orderId = rs.getInt("order_id");
                LocalDateTime orderDate = rs.getTimestamp("order_date").toLocalDateTime();
                double orderAmount = rs.getDouble("order_amount");
                String orderStatus = rs.getString("order_status");
                int orderQuantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                orders.add(new OrderAdminDTO(customerId,orderDate,orderAmount,orderStatus,orderId,orderQuantity,price));
            }
            connection.commit();
        }catch (Exception e){
            connection.rollback();
        }
        return orders;
    }
//    public int getOrderId() {
//        return orderId;
//    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}

record OrderAdminDTO(int CustomerId,LocalDateTime orderDate,double totalAmount,String orderStatus,int productId,int quantity,double price) {
}