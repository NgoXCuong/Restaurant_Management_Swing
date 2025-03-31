package model;

public class Ingredient {
    private int id_Ingredient;
    private String name;
    private int price;
    private String unit;
    // private int stock; // Cổ phần


    public Ingredient() {
    }

    public Ingredient(int id_Ingredient, String name, int price, String unit) {
        this.id_Ingredient = id_Ingredient;
        this.name = name;
        this.price = price;
        this.unit = unit;
    }

    public int getId_Ingredient() {
        return id_Ingredient;
    }

    public void setId_Ingredient(int id_Ingredient) {
        this.id_Ingredient = id_Ingredient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id_Ingredient=" + id_Ingredient +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                '}';
    }
}
