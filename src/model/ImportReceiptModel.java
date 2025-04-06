package model;

import java.util.Date;
import java.util.List;

public class ImportReceiptModel {
    private int id_Import;
    private int id_Employee;
    private Date date_Import;
    private int total;
    private List<ImportReceiptDetailModel> details;

    public ImportReceiptModel() {
    }

    public ImportReceiptModel(int id_Import, int id_Employee, Date date_Import, int total) {
        this.id_Import = id_Import;
        this.id_Employee = id_Employee;
        this.date_Import = date_Import;
        this.total = total;
    }

    public int getId_Import() {
        return id_Import;
    }

    public void setId_Import(int id_Import) {
        this.id_Import = id_Import;
    }

    public int getId_Employee() {
        return id_Employee;
    }

    public void setId_Employee(int id_Employee) {
        this.id_Employee = id_Employee;
    }

    public java.sql.Date getDate_Import() {
        return (java.sql.Date) date_Import;
    }

    public void setDate_Import(Date date_Import) {
        this.date_Import = date_Import;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ImportReceiptDetailModel> getDetails() {
        return details;
    }

    public void setDetails(List<ImportReceiptDetailModel> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "ImportReceipt{" +
                "id_Import=" + id_Import +
                ", id_Employee=" + id_Employee +
                ", date_Import=" + date_Import +
                ", total=" + total +
                '}';
    }
}
