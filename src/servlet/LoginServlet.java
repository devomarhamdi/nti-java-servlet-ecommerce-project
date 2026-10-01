package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.AuthService;

import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if(login == null || login.trim().isEmpty() || password == null || password.isEmpty()){
            fail(req, resp,"please enter your username and password");
            return;
        }

        try {
            User user = authService.login(login.trim(), password);

            if (user == null) {
                fail(req, resp, "login or password is incorrect");
                return;
            }

            HttpSession old = req.getSession(false);
            String redirectAfterLogin = null;
            if (old != null) {
                redirectAfterLogin = (String) old.getAttribute("redirectAfterLogin");
                old.invalidate();
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60);

            if(redirectAfterLogin != null){
                resp.sendRedirect(redirectAfterLogin);
                return;
            }
            if(user.isAdmin()){
                resp.sendRedirect(req.getContextPath()+"/admin/customers");
            } else{
                resp.sendRedirect(req.getContextPath()+"/products");
            }
        } catch (SQLException e) {
            log("login error", e);
            fail(req, resp, "something went wrong");
        }
    }


    private void fail(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("error", message);
        req.setAttribute("login", req.getParameter("login"));
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}