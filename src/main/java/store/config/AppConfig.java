package store.config;

import store.controller.StoreController;
import store.model.domain.Products;
import store.model.domain.Promotions;
import store.service.PaymentService;
import store.util.loader.ProductLoader;
import store.util.loader.PromotionLoader;
import store.view.InputView;
import store.view.OutputView;

public class AppConfig {
    public StoreController storeController() {
        return new StoreController(inputView(), outputView(), products(), promotions(), paymentService());
    }

    public InputView inputView() {
        return new InputView();
    }

    public OutputView outputView() {
        return new OutputView();
    }

    public Products products() {
        return new ProductLoader(promotions()).loadProducts();
    }

    public Promotions promotions() {
        return new PromotionLoader().loadPromotions();
    }

    public PaymentService paymentService() {
        return new PaymentService();
    }
}
