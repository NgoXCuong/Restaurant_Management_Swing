package model;

import java.util.Date;
import java.util.List;

public class ExportReceiptModel {
    private int id_Export;
    private int id_Employee;
    private Date date_Export;
    private List<ExportReceiptDetailModel> detailsExport;

    public ExportReceiptModel() {
    }

    public ExportReceiptModel(int id_Export, int id_Employee, Date date_Export) {
        this.id_Export = id_Export;
        this.id_Employee = id_Employee;
        this.date_Export = date_Export;
    }

    public int getId_Export() {
        return id_Export;
    }

    public void setId_Export(int id_Export) {
        this.id_Export = id_Export;
    }

    public int getId_Employee() {
        return id_Employee;
    }

    public void setId_Employee(int id_Employee) {
        this.id_Employee = id_Employee;
    }

    public Date getDate_Export() {
        return date_Export;
    }

    public void setDate_Export(Date date_Export) {
        this.date_Export = date_Export;
    }

    public List<ExportReceiptDetailModel> getDetailsExport() {
        return detailsExport;
    }

    public void setDetailsExport(List<ExportReceiptDetailModel> detailsExport) {
        this.detailsExport = detailsExport;
    }

    @Override
    public String toString() {
        return "ExportReceiptModel{" +
                "id_Export=" + id_Export +
                ", id_Employee=" + id_Employee +
                ", date_Export=" + date_Export +
                '}';
    }
}
