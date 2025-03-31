package model;

public class BillDetailModel {
    private int id_Bill;
    private int id_Dish;
    private int quantity;
    private int subTotal;
    private DishModel dish;

    public BillDetailModel() {}

    public BillDetailModel(int id_Bill, int id_Dish, int quantity, int subTotal) {
        this.id_Bill = id_Bill;
        this.id_Dish = id_Dish;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    public int getId_Bill() {
        return id_Bill;
    }

    public void setId_Bill(int id_Bill) {
        this.id_Bill = id_Bill;
    }

    public int getId_Dish() {
        return id_Dish;
    }

    public void setId_Dish(int id_Dish) {
        this.id_Dish = id_Dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }

    public DishModel getDish() {
        return dish;
    }

    public void setDish(DishModel dish) {
        this.dish = dish;
    }

    @Override
    public String toString() {
        return "BillDetailModel{" +
                "id_Bill=" + id_Bill +
                ", id_Dish=" + id_Dish +
                ", quantity=" + quantity +
                ", subTotal=" + subTotal +
                ", dish=" + dish +
                '}';
    }
}
