package servlet;

import dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Order;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * FR-25/FR-26/FR-38/FR-39: order history at /orders and a single order's
 * detail (with joined product names/prices) at /order.
 */
public class OrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object customerIdAttr = request.getSession().getAttribute("customerId");
        if (customerIdAttr == null) {
            request.getSession().setAttribute("flashError", "Please log in to view your orders.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        int customerId = (Integer) customerIdAttr;

        try {
            if ("/order".equals(request.getServletPath())) {
                showDetails(request, response, customerId);
            } else {
                showHistory(request, response, customerId);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error while loading orders", e);
        }
    }

    private void showHistory(HttpServletRequest request, HttpServletResponse response, int customerId)
            throws ServletException, IOException, SQLException {
        List<Order> orders = orderDAO.findByCustomer(customerId);
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/customer/orders.jsp").forward(request, response);
    }

    private void showDetails(HttpServletRequest request, HttpServletResponse response, int customerId)
            throws ServletException, IOException, SQLException {
        Order order = null;
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                order = orderDAO.findByIdWithItems(Integer.parseInt(idParam), customerId);
            } catch (NumberFormatException ignored) {
                // falls through to the "not found" handling below
            }
        }

        if (order == null) {
            request.setAttribute("errorMessage", "The requested order does not exist.");
            request.setAttribute("orders", orderDAO.findByCustomer(customerId));
            request.getRequestDispatcher("/customer/orders.jsp").forward(request, response);
            return;
        }

        request.setAttribute("order", order);
        request.getRequestDispatcher("/customer/order-details.jsp").forward(request, response);
    }
}
