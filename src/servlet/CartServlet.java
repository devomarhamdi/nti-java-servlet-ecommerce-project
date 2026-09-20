package servlet;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.CartItem;
import model.Product;

import java.io.IOException;
import java.sql.SQLException;

public class CartServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("cart", getCart(request));
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cart cart = getCart(request);
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                addToCart(request, cart);
            } else if ("remove".equals(action)) {
                cart.removeItem(Integer.parseInt(request.getParameter("productId")));
            } else if ("update".equals(action)) {
                cart.updateQuantity(Integer.parseInt(request.getParameter("productId")),
                        Integer.parseInt(request.getParameter("quantity")));
            } else if ("clear".equals(action)) {
                cart.clear();
            }
        } catch (SQLException e) {
            throw new ServletException("Database error while updating the cart", e);
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void addToCart(HttpServletRequest request, Cart cart) throws SQLException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        Product product = productDAO.findById(productId);
        if (product != null && quantity > 0) {
            cart.addItem(new CartItem(product, quantity));
        }
    }

    private Cart getCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
