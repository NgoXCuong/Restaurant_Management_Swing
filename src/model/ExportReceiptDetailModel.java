package model;

public class ExportReceiptDetailModel {
    private int id_Export;
    private int id_Ingredient;
    private int quantity;
    private IngredientModel ingredient;

    public ExportReceiptDetailModel() {
    }

    public ExportReceiptDetailModel(int id_Export, int id_Ingredient, int quantity) {
        this.id_Export = id_Export;
        this.id_Ingredient = id_Ingredient;
        this.quantity = quantity;
    }

    public int getId_Export() {
        return id_Export;
    }

    public void setId_Export(int id_Export) {
        this.id_Export = id_Export;
    }

    public int getId_Ingredient() {
        return id_Ingredient;
    }

    public void setId_Ingredient(int id_Ingredient) {
        this.id_Ingredient = id_Ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public IngredientModel getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientModel ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "ExportReceiptDetailModel{" +
                "id_Export=" + id_Export +
                ", id_Ingredient=" + id_Ingredient +
                ", quantity=" + quantity +
                '}';
    }
}
