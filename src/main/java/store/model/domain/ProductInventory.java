package store.model.domain;

import java.util.List;

public class ProductInventory {
    private final List<Product> products;

    public ProductInventory(List<Product> products) {
        this.products = products;
    }
}
