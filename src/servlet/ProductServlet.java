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
 * Handles customer-facing product browsing: the catalog list (with optional
 * name search, FR-8) at /products, and a single product's details (FR-9) at
 * /product. Admin CRUD lives in Hazem's AdminProductServlet, which calls
 * ProductDAO directly rather than going through this servlet.
 */
public class ProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if ("/product".equals(request.getServletPath())) {
                showDetails(request, response);
            } else {
                showList(request, response);
            }
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
        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }

    private void showDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        Product product = null;
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                product = productDAO.findById(Integer.parseInt(idParam));
            } catch (NumberFormatException ignored) {
                // falls through to the "not found" handling below
            }
        }

        if (product == null) {
            // FR-40: report when a requested product does not exist.
            request.setAttribute("errorMessage", "The requested product does not exist.");
            request.setAttribute("products", productDAO.findAll());
            request.getRequestDispatcher("/products.jsp").forward(request, response);
            return;
        }

        request.setAttribute("product", product);
        request.getRequestDispatcher("/product-details.jsp").forward(request, response);
    }
}
