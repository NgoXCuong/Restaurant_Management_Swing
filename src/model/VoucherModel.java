package model;

public class VoucherModel {
    private String codeVoucher;
    private String description;
    private int discountPercent;
    private String dishCategory;
    private int quantity;
    private int points;

    public VoucherModel() {
    }

    public VoucherModel(String codeVoucher, String description, int discountPercent, String dishCategory, int quantity, int points) {
        this.codeVoucher = codeVoucher;
        this.description = description;
        this.discountPercent = discountPercent;
        this.dishCategory = dishCategory;
        this.quantity = quantity;
        this.points = points;
    }

    public String getCodeVoucher() {
        return codeVoucher;
    }

    public void setCodeVoucher(String codeVoucher) {
        this.codeVoucher = codeVoucher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getDishCategory() {
        return dishCategory;
    }

    public void setDishCategory(String dishCategory) {
        this.dishCategory = dishCategory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "VoucherModel{" +
                "codeVoucher='" + codeVoucher + '\'' +
                ", description='" + description + '\'' +
                ", discountPercent=" + discountPercent +
                ", dishCategory='" + dishCategory + '\'' +
                ", quantity=" + quantity +
                ", points=" + points +
                '}';
    }
}
