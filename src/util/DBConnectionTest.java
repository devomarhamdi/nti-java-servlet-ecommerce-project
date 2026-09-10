package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Temporary sanity check for Phase 0 - confirms DBConnection can reach
 * PostgreSQL and that database.sql was run successfully.
 * Delete this class once the setup is verified.
 */
public class DBConnectionTest {

    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Connected to database successfully.");

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT name FROM categories ORDER BY id")) {
                System.out.println("Categories in database:");
                while (rs.next()) {
                    System.out.println(" - " + rs.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
