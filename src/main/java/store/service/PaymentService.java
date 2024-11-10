import store.model.domain.Products;

public class PaymentService {
    private final Products products;

    public PaymentService(Products products) {
        this.products = products;
    }
}
