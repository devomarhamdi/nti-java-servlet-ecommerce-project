package dao;

import model.Role;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserDAO{

    public record LoginRow(User user, String passwordHash){ }

    private static final String COLUMNS = "id, username, email, role, created_at";

    public LoginRow findForLogin(Connection conn, String usernameOrEmail) throws SQLException {

            String sql = "SELECT " + COLUMNS + ", password FROM users " + "WHERE username = ? OR LOWER(email) = LOWER(?)";

            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, usernameOrEmail.toLowerCase());
                ps.setString(2, usernameOrEmail.toLowerCase());

                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return null;
                    }
                    return new LoginRow(map(rs), rs.getString("password"));
                }
            }
    }

    private boolean exists(Connection conn, String sql, String value) throws SQLException {
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, value);
            try(ResultSet rs = ps.executeQuery()){
                return (rs.next());
            }
        }
    }

    public boolean usernameExists(Connection conn, String username) throws SQLException {
        return exists(conn, "select 1 from users where username = ?", username);
    }

    public boolean emailExists(Connection conn, String email) throws SQLException {
        return exists(conn, "select 1 from users where lower(email) = lower(?)", email);
    }

    public int insert(Connection conn, User user, String passwordHash) throws SQLException {

        String sql = "INSERT INTO users (username, email, password, role) "
                + "VALUES (?, ?, ?, ?::user_role) RETURNING id";

        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, user.getUsername().toLowerCase());
            ps.setString(2, user.getEmail().toLowerCase());
            ps.setString(3, passwordHash);
            ps.setString(4, user.getRole().name());

            try(ResultSet rs = ps.executeQuery()){
                rs.next();
                return rs.getInt("id");
            }
        }
    }

}