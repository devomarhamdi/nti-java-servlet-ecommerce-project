package model;

public class Customer {

    private int id;
    private User user;
    private String name;
    private String Phone;
    private String address;

    public Customer (){ }

    public Customer(int id, User user, String name, String phone, String address) {
        this.id = id;
        this.user = user;
        this.name = name;
        Phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}