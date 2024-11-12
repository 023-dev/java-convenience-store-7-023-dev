package store.service;

import static store.common.constant.Constants.EMPTY;
import static store.common.constant.Constants.LEFT_BRACKET;
import static store.common.constant.Constants.QUANTITY_DELIMITER;
import static store.common.constant.Constants.RIGHT_BRACKET;
import static store.util.ErrorMessage.*;

import java.util.ArrayList;
import java.util.List;

public class ValidationService {

    public void validateOrder (String input, List<String> productNames) {
        validateNullorBlank(input);
        List<String> items = parseItem(input);
        String productName = items.getFirst();
        String quantity = items.getLast();
        validateProductName(productName, productNames);
        validateQuantity(quantity);
    }

    private void validateNullorBlank (String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(EMPTY_INPUT.getMessage());
        }
    }

    public List<String> parseItem(String input) {
        List<String> items = new ArrayList<>();
        validateItemFormat(input);
        String parseInput =input.replace(LEFT_BRACKET, EMPTY).replace(RIGHT_BRACKET, EMPTY);
        for (String item : parseInput.split(QUANTITY_DELIMITER)) {
            items.add(item);
        }
        return items;
    }

    public void validateItemFormat(String item) {
        if (!item.startsWith(LEFT_BRACKET)&&item.contains(QUANTITY_DELIMITER)&&item.endsWith(RIGHT_BRACKET)) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }

    public void validateProductName(String productName, List<String> productNames) {
        if (!productNames.contains(productName)) {
            throw new IllegalArgumentException(PRODUCT_NOT_FOUND.getMessage());
        }
    }

    public static int validateQuantity(String item) {
        try {
            int quantity = Integer.parseInt(item);
            if (quantity <= 0) {
                throw new IllegalArgumentException(NON_POSITIVE_QUANTITY.getMessage());
            }
            return quantity;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_QUANTITY.getMessage());
        }
    }
}
