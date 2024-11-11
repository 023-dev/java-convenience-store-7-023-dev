package store.model.domain;

import static store.util.ErrorMessage.INSUFFICIENT_STOCK;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public Optional<Product> findByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equalsIgnoreCase(name))
                .findFirst();
    }


    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public List<String> getProductNames() {
        return products.stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    public Optional<Product> findProductByName(String productName, boolean prioritizePromotion) {
        return products.stream()
                .filter(product -> product.getName().equalsIgnoreCase(productName))
                .filter(product -> prioritizePromotion == product.hasPromotion())
                .findFirst();
    }

    public void deductStock(String productName, int quantity) {
        int remainingQuantity = quantity;
        Optional<Product> promoProduct = findProductByName(productName, true);
        if (promoProduct.isPresent()) {
            remainingQuantity -= promoProduct.get().deductStockUpTo(remainingQuantity);
        }
        if (remainingQuantity > 0) {
            Optional<Product> regularProduct = findProductByName(productName, false);
            if (regularProduct.isPresent()) {
                regularProduct.get().deductStockUpTo(remainingQuantity);
            } else {
                throw new IllegalStateException(INSUFFICIENT_STOCK.getMessage());
            }
        }
    }

    public Optional<Product> findPrioritizedProduct(String productName) {
        return products.stream()
                .filter(product -> product.getName().equalsIgnoreCase(productName))
                .sorted((p1, p2) -> Boolean.compare(p2.hasPromotion(), p1.hasPromotion()))
                .findFirst();
    }

    private int getTotalStock(String productName) {
        return products.stream()
                .filter(product -> product.getName().equalsIgnoreCase(productName))
                .mapToInt(Product::getStock)
                .sum();
    }

    public boolean hasSufficientStock(String productName, int quantity) {
        int totalAvailableStock = getTotalStock(productName);
        return totalAvailableStock >= quantity;
    }
}
