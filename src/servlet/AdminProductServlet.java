package servlet;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Admin product management (FR-28..FR-30) at /admin/products.
 * All product SQL goes through ProductDAO (Slice 3); this servlet only
 * coordinates forms, validation and messages.
 * GET lists products with optional ?keyword= search.
 */
public class AdminProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            showList(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error while loading products", e);
        }
    }

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String keyword = request.getParameter("keyword");
        List<Product> products;
        if (keyword != null && !keyword.isBlank()) {
            products = productDAO.searchByName(keyword.trim());
            request.setAttribute("keyword", keyword.trim());
        } else {
            products = productDAO.findAll();
        }
        request.setAttribute("products", products);
        request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
    }
}
