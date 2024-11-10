package store.config;

import store.controller.StoreController;
import store.model.domain.Promotions;
import store.util.loader.PromotionLoader;

public class AppConfig {
    public StoreController storeController() {
        return new StoreController(promotions());
    }

    public Promotions promotions() {
        return new PromotionLoader().loadPromotions();
    }
}
