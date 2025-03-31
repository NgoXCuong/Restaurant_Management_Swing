package model;

public class TableModel {
    private int id_Table;
    private String name;
    private String location;
    private String status;

    public TableModel() {
    }

    public TableModel(int id_Table, String name, String location, String status) {
        this.id_Table = id_Table;
        this.name = name;
        this.location = location;
        this.status = status;
    }

    public int getId_Table() {
        return id_Table;
    }

    public void setId_Table(int id_Table) {
        this.id_Table = id_Table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TableModel{" +
                "id_Table=" + id_Table +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
