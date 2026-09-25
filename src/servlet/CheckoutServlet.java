package servlet;

import dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;

import java.io.IOException;
import java.sql.SQLException;

/**
 * FR-11 to FR-19: reviews the session cart and turns it into a persisted
 * order. Relies on the login slice to place the logged-in customer's id in
 * session under "customerId" once a customer signs in; until that lands,
 * checkout redirects to login.jsp instead of guessing who the customer is.
 */
public class CheckoutServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        Cart cart = getCart(request);
        if (cart.getItems().isEmpty()) {
            request.getSession().setAttribute("flashError", "Your cart is empty.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        Cart cart = getCart(request);
        HttpSession session = request.getSession();
        Integer customerId = (Integer) session.getAttribute("customerId");

        try {
            int orderId = orderDAO.createOrder(customerId, cart);
            cart.clear();
            session.setAttribute("flashSuccess", "Order placed successfully!");
            response.sendRedirect(request.getContextPath() + "/order?id=" + orderId);
        } catch (OrderDAO.InsufficientStockException e) {
            session.setAttribute("flashError", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (IllegalArgumentException e) {
            session.setAttribute("flashError", "Your cart is empty.");
            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (SQLException e) {
            throw new ServletException("Database error while placing the order", e);
        }
    }

    private boolean requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object customerId = request.getSession().getAttribute("customerId");
        if (customerId == null) {
            request.getSession().setAttribute("flashError", "Please log in to checkout.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return false;
        }
        return true;
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
