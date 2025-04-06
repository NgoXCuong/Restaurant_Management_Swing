package model;

public class ImportReceiptDetailModel {
    private int id_Import;
    private int id_Ingredients;
    private int quantity;
    private int subTotal;
    private IngredientModel ingredient;

    public ImportReceiptDetailModel() {
    }

    public ImportReceiptDetailModel(int id_Import, int id_Ingredients, int quantity, int subTotal) {
        this.id_Import = id_Import;
        this.id_Ingredients = id_Ingredients;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    public int getId_Import() {
        return id_Import;
    }

    public void setId_Import(int id_Import) {
        this.id_Import = id_Import;
    }

    public int getId_Ingredients() {
        return id_Ingredients;
    }

    public void setId_Ingredients(int id_Ingredients) {
        this.id_Ingredients = id_Ingredients;
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

    public IngredientModel getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientModel ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "ImportReceiptDetailModel{" +
                "id_Import=" + id_Import +
                ", id_Ingredients=" + id_Ingredients +
                ", quantity=" + quantity +
                ", subTotal=" + subTotal +
                '}';
    }
}
