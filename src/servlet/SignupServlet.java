package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthService;
import service.DuplicateUserException;

import java.io.IOException;
import java.sql.SQLException;

public class SignupServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username").trim();
        String email = req.getParameter("email").trim();
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String name = req.getParameter("name").trim();
        String phone = req.getParameter("phone").trim();
        String address = req.getParameter("address").trim();

        String error = validate(username, email, password, confirmPassword, name, phone, address);

        if(error == null) {
            try{
                authService.register(username, email, password, name, phone, address);
                resp.sendRedirect(req.getContextPath() + "/login?registered=1");
                return;
            } catch(DuplicateUserException e){
                error = e.getMessage();
            } catch(SQLException e){
                log("Registeration failed: " + e);
                error = "registration faile";
            }
        }

        req.setAttribute("error", error);
        req.setAttribute("username", username);
        req.setAttribute("email", email);
        req.setAttribute("name", name);
        req.setAttribute("phone", phone);
        req.setAttribute("address", address);
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    private String validate(String username, String email, String password,
                            String confirmPassword, String name, String phone, String address) {

        if (username.isBlank() || email.isBlank() || password.isBlank()
                || name.isBlank() || phone.isBlank() || address.isBlank()) {
            return "All fields are required.";
        }
        if (username.length() < 3 || username.length() > 50) {
            return "Username must be between 3 and 50 characters.";
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return "Please enter a valid email address.";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters.";
        }
        if (!password.equals(confirmPassword)) {
            return "The two passwords do not match.";
        }
        if (phone.length() > 20) {
            return "Phone number is too long.";
        }
        if (address.length() > 255) {
            return "Address is too long (maximum 255 characters).";
        }
        return null;     // null means "no error"
    }

}
