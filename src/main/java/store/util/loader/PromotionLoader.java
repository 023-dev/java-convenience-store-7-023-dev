package store.util.loader;

import static store.common.constant.Constants.DELIMITER;
import static store.util.ErrorMessage.FILE_NOT_FOUNT_ERROR;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.model.domain.Promotion;
import store.model.domain.Promotions;
import store.util.ErrorMessage;

public class PromotionLoader {
    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";
    private static final int NAME_INDEX = 0;
    private static final int BUY_INDEX = 1;
    private static final int GET_INDEX = 2;
    private static final int START_DATE_INDEX = 3;
    private static final int END_DATE_INDEX = 4;

    public Promotions loadPromotions() {
        try (Stream<String> lines = Files.lines(Paths.get(PROMOTION_FILE_PATH))) {
            List<Promotion> promotions = lines.skip(1)
                    .map(this::parseLine)
                    .collect(Collectors.toList());
            return new Promotions(promotions);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(FILE_NOT_FOUNT_ERROR.getMessage());
        } catch (IOException e) {
            throw new IllegalArgumentException(ErrorMessage.FILE_READ_ERROR.getMessage());
        }
    }

    private Promotion parseLine(String line) {
        String[] fields = line.split(DELIMITER);
        String name = fields[NAME_INDEX];
        int buyQuantity = parseInteger(fields[BUY_INDEX]);
        int getQuantity = parseInteger(fields[GET_INDEX]);
        LocalDateTime startDate = parseDateTime(fields[START_DATE_INDEX]);
        LocalDateTime endDate = parseDateTime(fields[END_DATE_INDEX]);
        return new Promotion(name, buyQuantity, getQuantity, startDate, endDate);
    }

    private static int parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_NUMBER_FORMAT.getMessage());
        }
    }

    private static LocalDateTime parseDateTime(String value) {
        try {
            return LocalDate.parse(value).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_DATE_FORMAT.getMessage());
        }
    }
}
