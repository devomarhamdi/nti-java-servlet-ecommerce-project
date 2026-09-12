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
 * Serves the public landing page at "/" with a small set of featured
 * products. The full catalog lives behind /products (ProductServlet).
 */
public class HomeServlet extends HttpServlet {

    private static final int FEATURED_LIMIT = 8;

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Product> products = productDAO.findAll();
            if (products.size() > FEATURED_LIMIT) {
                products = products.subList(0, FEATURED_LIMIT);
            }
            request.setAttribute("products", products);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error while loading the home page", e);
        }
    }
}
