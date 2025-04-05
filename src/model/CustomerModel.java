package model;

import java.util.Date;

public class CustomerModel {
    private int id_Customer;
    private String name;
    private Date joinDate;
    private int revenue;
    private int points;
    private int id_User;

    public CustomerModel() {
    }

    public CustomerModel(int id_Customer, String name, Date joinDate, int revenue, int points, int id_User) {
        this.id_Customer = id_Customer;
        this.name = name;
        this.joinDate = joinDate;
        this.revenue = revenue;
        this.points = points;
        this.id_User = id_User;
    }

    public int getId_Customer() {
        return id_Customer;
    }

    public void setId_Customer(int id_Customer) {
        this.id_Customer = id_Customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.sql.Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getId_User() {
        return id_User;
    }

    public void setId_User(int id_User) {
        this.id_User = id_User;
    }

    @Override
    public String toString() {
        return "CustomerModel{" +
                "id_Customer=" + id_Customer +
                ", name='" + name + '\'' +
                ", joinDate=" + joinDate +
                ", revenue=" + revenue +
                ", points=" + points +
                ", id_User=" + id_User +
                '}';
    }
}
