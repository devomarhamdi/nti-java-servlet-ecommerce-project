package dao;

import model.*;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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