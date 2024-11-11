package store.model.domain;

import java.util.Optional;

public class Product {
    private final String name;
    private final int price;
    private int stock;
    private final Optional<Promotion> promotion;

    public Product(String name, int price, int stock, Optional<Promotion> promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
    }

    public int getActualQuantity(int orderedQuantity) {
        if (promotion.isEmpty()) return orderedQuantity;
        Promotion promotion = this.promotion.get();
        return promotion.calculatePaidQuantity(orderedQuantity);
    }

    public int getFreeQuantity(int orderedQuantity) {
        if (promotion.isEmpty()) return 0;
        Promotion promo = promotion.get();
        return promo.calculateFreeQuantity(orderedQuantity);
    }

    public int calculateTotalPrice(int quantity) {
        return price * quantity;
    }

    public int deductStockUpTo(int quantity) {
        int deductQuantity = Math.min(quantity, stock);
        stock -= deductQuantity;
        return deductQuantity;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public Optional<Promotion> getPromotion() {
        return promotion;
    }

    public boolean hasPromotion() {
        return promotion.isPresent();
    }
}
