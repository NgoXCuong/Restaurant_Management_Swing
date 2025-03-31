package model;

public class UserModel {
    private int id_User;
    private String email;
    private String password;
    private String verifyCode;
    private String status;
    private String role;

    public UserModel() {
    }

    public UserModel(int id_User, String email, String password, String verifyCode, String status, String role) {
        this.id_User = id_User;
        this.email = email;
        this.password = password;
        this.verifyCode = verifyCode;
        this.status = status;
        this.role = role;
    }

    public int getId_User() {
        return id_User;
    }

    public void setId_User(int id_User) {
        this.id_User = id_User;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id_User=" + id_User +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", status='" + status + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
