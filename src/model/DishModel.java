package model;

public class DishModel {
    private int id_Dish;
    private String name_Dish;
    private int price;
    private String category;
    private String status;

    public DishModel()  {}

    public DishModel(int id_Dish, String name_Dish, int price, String category, String status) {
        this.id_Dish = id_Dish;
        this.name_Dish = name_Dish;
        this.price = price;
        this.category = category;
        this.status = status;
    }

    public int getId_Dish() {
        return id_Dish;
    }

    public void setId_Dish(int id_Dish) {
        this.id_Dish = id_Dish;
    }

    public String getName_Dish() {
        return name_Dish;
    }

    public void setName_Dish(String name_Dish) {
        this.name_Dish = name_Dish;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DishModel{" +
                "id_Dish=" + id_Dish +
                ", name_Dish='" + name_Dish + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
