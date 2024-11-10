package store.controller;

import store.model.domain.Products;
import store.model.domain.Promotions;
import store.view.InputView;
import store.view.OutputView;
import store.service.PaymentService;

public class StoreController {
    private static final String YES = "Y";

    private final InputView inputView;
    private final OutputView outputView;
    private final Products products;
    private final Promotions promotions;
    private final PaymentService paymentService;

    public StoreController(InputView inputView,OutputView outputView, Products products, Promotions promotion, PaymentService paymentService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.products = products;
        this.promotions = promotion;
        this.paymentService = paymentService;
    }

    public void run() {
        boolean continueShopping = true;
        while (continueShopping) {
            outputView.printWelcomeMessage();
            outputView.printProducts(products);
            continueShopping = askToContinueShopping();
        }
    }

    private boolean askToContinueShopping() {
        String answer = inputView.readAdditionalPurchase();
        return answer.equalsIgnoreCase(YES);
    }
}