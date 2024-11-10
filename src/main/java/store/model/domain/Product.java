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
}
