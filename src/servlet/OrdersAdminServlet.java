package servlet;

import dao.OrderDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Customer;

import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;

@WebServlet("/admin/orders")
class  OrdersAdminServlet extends HttpServlet {
	@Serial
    private static final long serialVersionUID = 1L;
    ServletContext servletContext;
    private OrderDAO  orderDAO = (OrderDAO) servletContext.getAttribute("OrderDAO");

    @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("orders",orderDAO.getAllOrdersToAdmin());
        } catch (SQLException e) {
            //TODO: Error Page
            response.sendRedirect("");
        }
    }
}