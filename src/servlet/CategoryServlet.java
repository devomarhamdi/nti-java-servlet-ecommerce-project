package servlet;

import dao.CategoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Admin category management (FR-31) at /admin/categories.
 * GET lists all categories; POST handles form actions selected by the
 * "action" parameter (create; update/delete added incrementally).
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                create(request);
            } else {
                request.getSession().setAttribute("flashError", "Unknown category action.");
            }
        } catch (SQLException e) {
            // Postgres 23505 = unique_violation on categories.name
            String msg = "23505".equals(e.getSQLState())
                    ? "A category with that name already exists."
                    : "Could not save the category. Please try again.";
            request.getSession().setAttribute("flashError", msg);
        }
        // Redirect after POST so a browser refresh does not resubmit the form.
        response.sendRedirect(request.getContextPath() + "/admin/categories");
    }

    private void create(HttpServletRequest request) throws SQLException {
        Category category = readForm(request);
        if (category == null) {
            return;
        }
        categoryDAO.insert(category);
        request.getSession().setAttribute("flashSuccess",
                "Category \"" + category.getName() + "\" created.");
    }

    /**
     * Builds a Category from the form fields. Returns null (and sets a flash
     * error) when the name is missing.
     */
    private Category readForm(HttpServletRequest request) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        if (name == null || name.isBlank()) {
            request.getSession().setAttribute("flashError", "Category name is required.");
            return null;
        }
        Category category = new Category();
        category.setName(name.trim());
        category.setDescription(description == null || description.isBlank() ? null : description.trim());
        return category;
    }
}
