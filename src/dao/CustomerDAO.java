package dao;

import model.Customer;
import model.Role;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    private static final String SELECT_BASE = "select c.id, c.name, c.email, c.phone, c.address, c.registration_data, " +
    "   u.id as uid, u.username, u.role " + "FROM customers c " + "JOIN users ON u.is = c.user_id ";

    public boolean emailExists(Connection conn, String email) throws SQLException {

        String sql = "select 1 from customers where lower(email) = lower(?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
             ps.setString(1, email);
             try(ResultSet rs = ps.executeQuery()){
                 return rs.next();
             }
        }
    }

    public void insert(Connection conn, int userId, Customer customer) throws SQLException {

        String sql = "INSERT INTO customers (user_id, name, email, phone, address )VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, userId);
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getAddress());
            ps.executeUpdate();
        }
    }

    public Customer findByUserId(Connection conn, int userId) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(SELECT_BASE + " WHERE user_id = ?")){
            ps.setInt(1, userId);
            try(ResultSet rs = ps.executeQuery()){
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Customer> findAllCustomers(Connection conn) throws SQLException {
        List<Customer> result = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(SELECT_BASE + " ORDER BY c.id")){
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                result.add(map(rs));
            }
        }
        return result;
    }

    private Customer map(ResultSet rs) throws SQLException {

        User user = new User(
                rs.getInt("uid"),
                rs.getString("username"),
                Role.valueOf(rs.getString("role").toUpperCase())
        );
        return new Customer(
          rs.getInt("id"),
          user,
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getTimestamp("registration_date")
        );

    }

}