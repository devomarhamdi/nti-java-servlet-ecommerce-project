package servlet;

import dao.CategoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Admin category management (FR-31) at /admin/categories.
 * GET lists all categories; POST actions (create/update/delete) are added
 * incrementally.
 */
public class CategoryServlet extends HttpServlet {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("categories", categoryDAO.findAll());
            request.getRequestDispatcher("/admin/categories.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error while loading categories", e);
        }
    }
}
