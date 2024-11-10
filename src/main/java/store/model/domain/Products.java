package store.model.domain;

import java.util.ArrayList;
import java.util.List;
import store.model.dto.ProductDto;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }
}
