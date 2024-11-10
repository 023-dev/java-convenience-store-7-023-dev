package store.config;

import store.controller.StoreController;

public class AppConfig {
    public StoreController storeController() {
        return new StoreController();
    }
}
