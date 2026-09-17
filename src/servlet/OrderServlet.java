package servlet;

import dao.OrderDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.Customer;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/orders")
class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    ServletContext servletContext;
    HttpSession session;
    //TODO: Put The "OrderDAO" bean in Context.
    private OrderDAO  orderDAO = (OrderDAO) servletContext.getAttribute("OrderDAO");
    private Customer customer = (Customer) session.getAttribute("customer");

    //user Order History
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            req.setAttribute("orders",orderDAO.getAllOrdersToCustomer(customer));
            req.getRequestDispatcher("/WEB-INF/jsp/order.jsp").forward(req,resp);
        }catch (SQLException e){
            //TODO: Error Page
            resp.sendRedirect("");
        }
    }

    //INFO:==> Place Order Controller
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        try {
            orderDAO.createOrder(cart,customer);
            //TODO: set the default success page
            response.sendRedirect("");
        }catch (SQLException e){
            //TODO: redirect to Error Page
            response.sendRedirect("");
        }
    }
}