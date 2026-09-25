package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Customer;
import service.AuthService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminCustomerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Customer> customers = authService.listCustomers();
            req.setAttribute("customers", customers);
        }
        catch (SQLException e) {
            log("Couldn't list customers", e);
            req.setAttribute("error", "Couldn't list customers");
        }
        req.getRequestDispatcher("/admin/customer.jsp").forward(req, resp);
    }
}
