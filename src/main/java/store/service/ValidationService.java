package store.service;

import static store.common.Constants.EMPTY;
import static store.common.Constants.LEFT_BRACKET;
import static store.common.Constants.QUANTITY_DELIMITER;
import static store.common.Constants.RIGHT_BRACKET;
import static store.util.ErrorMessage.*;

import java.util.List;
import java.util.regex.Pattern;

public class ValidationService {
    private static final String ITEM_PATTERN_REGEX = "\\[(\\w+)-(\\d+)]";
    private static final Pattern VALID_ITEM_PATTERN = Pattern.compile(ITEM_PATTERN_REGEX);

    public void validateInput (String input, List<String> productNames) {
        validateNullorBlank(input);
        validateItemFormat(input);
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

    private List<String> parseItem(String input) {
        String parseInput =input.replace(LEFT_BRACKET, EMPTY).replace(RIGHT_BRACKET, EMPTY);
        return List.of(parseInput.split(QUANTITY_DELIMITER));
    }

    private void validateItemFormat(String item) {
        if (!VALID_ITEM_PATTERN.matcher(item).matches()) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }

    private void validateProductName(String productName, List<String> productNames) {
        if (!productNames.contains(productName)) {
            throw new IllegalArgumentException(PRODUCT_NOT_FOUND.getMessage());
        }
    }

    private static int validateQuantity(String item) {
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
