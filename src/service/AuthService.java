package service;

import dao.CustomerDAO;
import dao.UserDAO;
import model.Customer;
import model.Role;
import model.User;
import util.DBConnection;
import util.PasswordUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    public User register(String username, String email,
                         String plainPassword, String name, String phone, String address) throws SQLException, DuplicateUserException {
        try(Connection conn = DBConnection.getConnection()){

            conn.setAutoCommit(false);
            try{
                if(userDAO.usernameExists(conn, username)){
                    throw new DuplicateUserException("username", "this uername is already taken");
                }
                if(customerDAO.emailExists(conn, email)){
                    throw new DuplicateUserException("email", "this email is already taken");
                }

                User user = new User();
                user.setUsername(username);
                user.setRole(Role.CUSTOMER);

                String hash = PasswordUtil.hash(plainPassword);
                int userId = userDAO.insert(conn, user, hash);
                user.setId(userId);

                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(email);
                customer.setPhone(phone);
                customer.setAddress(address);
                customerDAO.insert(conn, userId, customer);

                conn.commit();
                return user;

            } catch(SQLException e){
                conn.rollback();
                if ("23505".equals(e.getSQLState())) {
                    String detail = String.valueOf(e.getMessage()).toLowerCase();
                    if(detail.contains("email")){
                        throw new DuplicateUserException("email", "this email is already taken");
                    }
                    throw new DuplicateUserException("username", "this uername is already taken");
                }
                throw e;

            } catch(DuplicateUserException e){
                conn.rollback();
                throw e;
            }
        }
    }

    public User login(String usernameOrEmail, String plainPassword) throws SQLException{

        try(Connection conn = DBConnection.getConnection()){
            UserDAO.LoginRow row = userDAO.findForLogin(conn, usernameOrEmail);
            if (row == null){
                return null;
            }
            if(!PasswordUtil.matches(plainPassword, row.getPasswordHash())){
                return null;
            }
            return row.getUser();
        }
    }

    public List<Customer> listCustomers() throws SQLException{
        try(Connection conn = DBConnection.getConnection()){
            return customerDAO.findAllCustomers(conn);
        }
    }

    public Customer findCustomerByUserId(int userId) throws SQLException{
        try(Connection conn = DBConnection.getConnection()){
            return customerDAO.findByUserId(conn, userId);
        }
    }

}
