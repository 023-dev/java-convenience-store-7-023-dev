package store.controller;

import static store.common.constant.Constants.DELIMITER;
import static store.common.constant.Constants.EMPTY;
import static store.common.constant.Constants.LEFT_BRACKET;
import static store.common.constant.Constants.QUANTITY_DELIMITER;
import static store.common.constant.Constants.RIGHT_BRACKET;
import static store.util.ErrorMessage.ORDER_CANCELED;
import static store.util.ErrorMessage.PRODUCT_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.model.domain.Order;
import store.model.domain.Product;
import store.model.domain.Products;
import store.model.domain.Promotion;
import store.model.domain.Promotions;
import store.model.domain.Receipt;
import store.service.ValidationService;
import store.view.InputView;
import store.view.OutputView;
import store.service.PaymentService;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;
    private final Products products;
    private final Promotions promotions;
    private final PaymentService paymentService;
    private final ValidationService validationService;

    public StoreController(InputView inputView,OutputView outputView, Products products, Promotions promotion, PaymentService paymentService, ValidationService validationService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.products = products;
        this.promotions = promotion;
        this.paymentService = paymentService;
        this.validationService = validationService;
    }

    public void run() {
        boolean continueShopping = true;
        while (continueShopping) {
            startShopping();
            try {
                List<Order> orders = getOrders();
                boolean isMember = askToMembership();
                processPurchase(orders, isMember);
                continueShopping = askToContinueShopping();
            } catch (IllegalArgumentException | IllegalStateException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private void startShopping() {
        outputView.printWelcomeMessage();
        outputView.printProducts(products);
    }

    private List<Order> getOrders() {
        while(true){
            try {
                String input = inputView.readItem();
                List<Order> orders = validateAndParseOrders(input);
                return orders;
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private List<Order> validateAndParseOrders(String input) {
        List<Order> orders = new ArrayList<>();
        for (String item : input.split(DELIMITER)) {
            validationService.validateOrder(item, products.getProductNames());
            Order order = parseOrder(item);
            orders.add(order);
        }
        validateStock(orders);
        confirmOrdersWithPromotion(orders);
        return orders;
    }

    private Order parseOrder(String item) {
        List<String> splitItem = parseItem(item);
        String productName = splitItem.getFirst();
        int quantity = Integer.parseInt(splitItem.getLast());
        Optional<Product> productOpt = products.findProductByName(productName, true);
        if (productOpt.isPresent() && productOpt.get().hasPromotion()) {
            askFreeQuantity(productName, quantity);
        }
        return new Order(productName, quantity);
    }

    private void confirmOrdersWithPromotion(List<Order> orders) {
        for (Order order : orders) {
            Product product = products.findPrioritizedProduct(order.getProductName()).get();
            int nonPromoQuantity = order.getQuantity() - product.getStock();
            if (nonPromoQuantity > 0) {
                askPromotionAvailability(product.getName(), nonPromoQuantity);
            }
        }
    }

    private void askPromotionAvailability(String productName, int nonPromoQuantity) {
        if (!inputView.readPromotionShortage(productName, nonPromoQuantity)) {
            throw new IllegalArgumentException(ORDER_CANCELED.getMessage());
        }
    }

    private List<String> parseItem(String input) {
        String parseInput =input.replace(LEFT_BRACKET, EMPTY).replace(RIGHT_BRACKET, EMPTY);
        return List.of(parseInput.split(QUANTITY_DELIMITER));
    }

    private Order askFreeQuantity(String productName, int quantity){
        Product product = products.findProductByName(productName, true)
                .orElseThrow(() -> new IllegalArgumentException(PRODUCT_NOT_FOUND.getMessage()));
        Promotion promotion = product.getPromotion().get();
        if (promotion != null && promotion.isAdditionalFreeItem(quantity)) {
            if (askPromotion(productName)) {
                quantity += promotion.getFreeQuantity();
            }
        }
        return new Order(productName, quantity);
    }

    private boolean askPromotion(String productName) {
        while(true){
            try {
                return inputView.readPromotion(productName);
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private boolean askToMembership() {
        while(true){
            try {
                return inputView.readMembership();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private void processPurchase(List<Order> orders, boolean isMember) {
        Receipt receipt = paymentService.checkout(products, orders, isMember);
        outputView.printReceipt(receipt);
    }

    private boolean askToContinueShopping() {
        while(true){
            try {
                return inputView.readContinue();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private void validateStock(List<Order> orders) {
        for (Order order : orders) {
            if (!products.hasSufficientStock(order.getProductName(), order.getQuantity())) {
                throw new IllegalStateException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
            }
        }
    }
}