package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DBConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Temporary sanity check for Phase 0 - confirms the app deploys on Tomcat
 * 10.0.27 (Jakarta Servlet 5.0) and can reach the database through
 * util.DBConnection. Delete this class and its web.xml mapping once the
 * setup is verified.
 */
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h1>Servlet OK - Jakarta Servlet 5.0 / Tomcat 10.0.27</h1>");

            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT name FROM categories ORDER BY id")) {

                out.println("<h2>Database connection OK. Categories:</h2>");
                out.println("<ul>");
                while (rs.next()) {
                    out.println("<li>" + rs.getString("name") + "</li>");
                }
                out.println("</ul>");
            } catch (Exception e) {
                out.println("<h2 style=\"color:red\">Database connection failed:</h2>");
                out.println("<pre>" + e.getMessage() + "</pre>");
            }

            out.println("</body></html>");
        }
    }
}
