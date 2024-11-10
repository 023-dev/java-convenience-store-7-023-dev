package store.service;

import static store.common.Constants.DELIMITER;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.util.ErrorMessage;

public class ValidationService {
    private static final String ITEM_PATTERN_REGEX = "\\[(\\w+)-(\\d+)]";
    private static final Pattern VALID_ITEM_PATTERN = Pattern.compile(ITEM_PATTERN_REGEX);

    public void validateInput (String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(ErrorMessage.EMPTY_INPUT.getMessage());
        }
        List<String> items = parseInput(input);
    }

    private static List<String> parseInput(String input) {
        return Stream.of(input.split(DELIMITER))
                .map(String::trim)
                .collect(Collectors.toList());
    }

}
