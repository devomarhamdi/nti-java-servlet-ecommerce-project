package dao;

import model.Role;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static class LoginRow {

        private final User user;
        private final String passwordHash;

        public LoginRow(User user, String passwordHash) {
            this.user = user;
            this.passwordHash = passwordHash;
        }

        public User getUser() { return user; }
        public String getPasswordHash() { return passwordHash; }
    }


    public LoginRow findForLogin(Connection conn, String usernameOrEmail)
            throws SQLException {

        String sql =
                "SELECT u.id, u.username, u.role, u.password " +
                        "FROM users u " +
                        "LEFT JOIN customers c ON c.user_id = u.id " +
                        "WHERE u.username = ? OR LOWER(c.email) = LOWER(?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;                 // no such user
                }
                return new LoginRow(map(rs), rs.getString("password"));
            }
        }
    }

    public boolean usernameExists(Connection conn, String username) throws SQLException {

        String sql = "SELECT 1 FROM users WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();                // any row at all = taken
            }
        }
    }

    public int insert(Connection conn, User user, String passwordHash) throws SQLException {

        String sql = "INSERT INTO users (username, password, role) "
                + "VALUES (?, ?, ?) RETURNING id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, passwordHash);
            ps.setString(3, user.getRole().name().toLowerCase());   // "customer"

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("id");
            }
        }
    }


    /** The ONE place that knows how a users row becomes a User object. */
    static User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                // "customer" in the database → Role.CUSTOMER in Java
                Role.valueOf(rs.getString("role").toUpperCase())
        );
    }
}