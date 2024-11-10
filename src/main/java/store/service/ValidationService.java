package store.service;

import static store.common.Constants.DELIMITER;
import static store.util.ErrorMessage.*;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationService {
    private static final String ITEM_PATTERN_REGEX = "\\[(\\w+)-(\\d+)]";
    private static final Pattern VALID_ITEM_PATTERN = Pattern.compile(ITEM_PATTERN_REGEX);

    public void validateInput (String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(EMPTY_INPUT.getMessage());
        }
        List<String> items = parseInput(input);
        items.forEach(this::validateItemFormat);
    }

    private List<String> parseInput(String input) {
        return Stream.of(input.split(DELIMITER))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private void validateItemFormat(String item) {
        if (!VALID_ITEM_PATTERN.matcher(item).matches()) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }
}
