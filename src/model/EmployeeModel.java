package model;

import java.util.Date;

public class EmployeeModel {
    private int id_Employee;
    private String name;
    private Date hireDate;
    private String phone;
    private String position;
    // id cho phép null
    private Integer id_User;
    private Integer id_Manager;
    private String status;

    public EmployeeModel() {
    }

    public EmployeeModel(int id_Employee, String name, Date hireDate, String phone, String position, Integer id_User, Integer id_Manager, String status) {
        this.id_Employee = id_Employee;
        this.name = name;
        this.hireDate = hireDate;
        this.phone = phone;
        this.position = position;
        this.id_User = id_User;
        this.id_Manager = id_Manager;
        this.status = status;
    }

    public int getId_Employee() {
        return id_Employee;
    }

    public void setId_Employee(int id_Employee) {
        this.id_Employee = id_Employee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.sql.Date getHireDate() {
        return (java.sql.Date) hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getId_User() {
        return id_User;
    }

    public void setId_User(Integer id_User) {
        this.id_User = id_User;
    }

    public Integer getId_Manager() {
        return id_Manager;
    }

    public void setId_Manager(Integer id_Manager) {
        this.id_Manager = id_Manager;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EmployeeModel{" +
                "id_Employee=" + id_Employee +
                ", name='" + name + '\'' +
                ", hireDate=" + hireDate +
                ", phone='" + phone + '\'' +
                ", position='" + position + '\'' +
                ", id_User=" + id_User +
                ", id_Manager=" + id_Manager +
                ", status='" + status + '\'' +
                '}';
    }
}
