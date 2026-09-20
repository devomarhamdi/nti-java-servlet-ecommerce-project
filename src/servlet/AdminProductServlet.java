package servlet;

import dao.CategoryDAO;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Admin product management (FR-28..FR-30) at /admin/products.
 * All product SQL goes through ProductDAO (Slice 3); this servlet only
 * coordinates forms, validation and messages.
 * GET lists products with optional ?keyword= search; GET ?action=new shows
 * the create form. POST actions are selected by the "action" parameter.
 */
public class AdminProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if ("new".equals(request.getParameter("action"))) {
                showForm(request, response, null);
            } else {
                showList(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error while loading products", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                create(request, response);
            } else {
                request.getSession().setAttribute("flashError", "Unknown product action.");
                response.sendRedirect(request.getContextPath() + "/admin/products");
            }
        } catch (SQLException e) {
            request.getSession().setAttribute("flashError",
                    "Could not save the product. Please try again.");
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }

    private void create(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        Product product = new Product();
        String error = readForm(request, product);
        if (error != null) {
            // Re-show the form with the typed values and the error, no redirect,
            // so the admin does not lose what they entered.
            request.setAttribute("errorMessage", error);
            showForm(request, response, product);
            return;
        }
        productDAO.insert(product);
        request.getSession().setAttribute("flashSuccess",
                "Product \"" + product.getName() + "\" created.");
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }

    /** Loads the category list (for the dropdown) and forwards to the form. */
    private void showForm(HttpServletRequest request, HttpServletResponse response, Product product)
            throws ServletException, IOException, SQLException {
        request.setAttribute("categories", categoryDAO.findAll());
        request.setAttribute("product", product);
        request.getRequestDispatcher("/admin/product-form.jsp").forward(request, response);
    }

    /**
     * Copies and validates the form fields into the given product.
     * Returns a user-friendly error message, or null when everything is valid.
     */
    private String readForm(HttpServletRequest request, Product product) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceRaw = request.getParameter("price");
        String stockRaw = request.getParameter("stock");
        String categoryRaw = request.getParameter("categoryId");

        product.setName(name == null ? "" : name.trim());
        product.setDescription(description == null || description.isBlank() ? null : description.trim());

        if (product.getName().isEmpty()) {
            return "Product name is required.";
        }
        try {
            product.setPrice(new BigDecimal(priceRaw.trim()));
        } catch (NumberFormatException | NullPointerException e) {
            return "Price must be a number.";
        }
        if (product.getPrice().signum() < 0) {
            return "Price cannot be negative.";
        }
        try {
            product.setStock(Integer.parseInt(stockRaw.trim()));
        } catch (NumberFormatException | NullPointerException e) {
            return "Stock must be a whole number.";
        }
        if (product.getStock() < 0) {
            return "Stock cannot be negative.";
        }
        try {
            product.setCategoryId(Integer.parseInt(categoryRaw));
        } catch (NumberFormatException | NullPointerException e) {
            return "Please choose a category.";
        }
        return null;
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
