package store.controller;

import static store.common.Constants.DELIMITER;
import static store.common.Constants.EMPTY;
import static store.common.Constants.LEFT_BRACKET;
import static store.common.Constants.QUANTITY_DELIMITER;
import static store.common.Constants.RIGHT_BRACKET;
import static store.util.ErrorMessage.INVALID_FORMAT;
import static store.util.ErrorMessage.INVALID_INPUT_FORMAT;
import static store.util.ErrorMessage.INVALID_QUANTITY;
import static store.util.ErrorMessage.NON_POSITIVE_QUANTITY;
import static store.util.ErrorMessage.PRODUCT_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import store.model.domain.Order;
import store.model.domain.Products;
import store.model.domain.Promotions;
import store.service.ValidationService;
import store.view.InputView;
import store.view.OutputView;
import store.service.PaymentService;

public class StoreController {
    private static final String YES = "Y";;

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
                String input = inputView.readItemInput();
                return validateAndParseOrders(input);
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private List<Order> validateAndParseOrders(String input) {
        List<Order> orders = new ArrayList<>();
        for (String item : input.split(DELIMITER)) {
            validationService.validateInput(item, products.getProductNames());
            orders.add(parseOrder(item));
        }
        return orders;
    }

    private Order parseOrder(String item) {
        List<String> splitItem = parseItem(item);
        String productName = splitItem.getFirst();
        int quantity = Integer.parseInt(splitItem.getLast());
        return new Order(productName, quantity);
    }

    private List<String> parseItem(String input) {
        String parseInput =input.replace(LEFT_BRACKET, EMPTY).replace(RIGHT_BRACKET, EMPTY);
        return List.of(parseInput.split(QUANTITY_DELIMITER));
    }

    private boolean askToMembership() {
        String answer = inputView.readMembership();
        return answer.equalsIgnoreCase(YES);
    }

    private boolean askToContinueShopping() {
        String answer = inputView.readAdditionalPurchase();
        return answer.equalsIgnoreCase(YES);
    }
}