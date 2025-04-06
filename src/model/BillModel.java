package model;

import java.util.Date;
import java.util.List;

public class BillModel {
    private int id;
    private int id_Customer;
    private int id_Table;
    private Date dateBill;
    private int foodMoney;
    private String codeVoucher;
    private int discount;
    private int totalAmount;
    private String status;
    private List<BillDetailModel> details;

    public BillModel() {}

    public BillModel(int id, int id_Customer, int id_Table, Date dateBill, int foodMoney, String codeVoucher, int discount, int totalAmount, String status) {
        this.id = id;
        this.id_Customer = id_Customer;
        this.id_Table = id_Table;
        this.dateBill = dateBill;
        this.foodMoney = foodMoney;
        this.codeVoucher = codeVoucher;
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_Customer() {
        return id_Customer;
    }

    public void setId_Customer(int id_Customer) {
        this.id_Customer = id_Customer;
    }

    public int getId_Table() {
        return id_Table;
    }

    public void setId_Table(int id_Table) {
        this.id_Table = id_Table;
    }

    public java.sql.Date getDateBill() {
        return (java.sql.Date) dateBill;
    }

    public void setDateBill(Date dateBill) {
        this.dateBill = dateBill;
    }

    public int getFoodMoney() {
        return foodMoney;
    }

    public void setFoodMoney(int foodMoney) {
        this.foodMoney = foodMoney;
    }

    public String getCodeVoucher() {
        return codeVoucher;
    }

    public void setCodeVoucher(String codeVoucher) {
        this.codeVoucher = codeVoucher;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BillDetailModel> getDetails() {
        return details;
    }

    public void setDetails(List<BillDetailModel> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "BillModel{" +
                "id=" + id +
                ", id_Customer=" + id_Customer +
                ", id_Table=" + id_Table +
                ", dateBill=" + dateBill +
                ", foodMoney=" + foodMoney +
                ", codeVoucher='" + codeVoucher + '\'' +
                ", discount=" + discount +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", details=" + details +
                '}';
    }
}
