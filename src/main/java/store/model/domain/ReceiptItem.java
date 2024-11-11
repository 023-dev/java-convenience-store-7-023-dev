package store.model.domain;

public class ReceiptItem {
    private final String name;
    private final int quantity;
    private final int price;
    private final boolean isFreeItem;

    public ReceiptItem(String name, int quantity, int price, boolean isFreeItem) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.isFreeItem = isFreeItem;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public boolean isFreeItem() {
        return isFreeItem;
    }

    @Override
    public String toString() {
        return String.format("%s\t\t%d\t%d", name, quantity, price);
    }
}
