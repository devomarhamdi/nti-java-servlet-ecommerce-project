package model;

public class User {

    private int id;
    private String username;
    private Role role;

    public User() { }   // required by JSP — keep it even with the big one below

    public User(int id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    /** Convenience for JSP: ${sessionScope.user.admin} resolves to this. */
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }
}
