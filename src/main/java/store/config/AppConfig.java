package store.config;

import store.controller.StoreController;
import store.model.domain.Products;
import store.model.domain.Promotions;
import store.util.loader.ProductLoader;
import store.util.loader.PromotionLoader;

public class AppConfig {
    public StoreController storeController() {
        return new StoreController(promotions(), products(), paymentService());
    }

    public Promotions promotions() {
        return new PromotionLoader().loadPromotions();
    }

    public Products products() {
        return new ProductLoader(promotions()).loadProducts();
    }

    public PaymentService paymentService() {
        return new PaymentService();
    }
}
