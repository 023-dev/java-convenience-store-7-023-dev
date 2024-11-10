package store.util.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.model.domain.Promotion;
import store.model.domain.Promotions;
import store.util.ErrorMessage;

public class PromotionLoader {

    protected static final String DELIMITER = ",";

    private static final int NAME_INDEX = 0;
    private static final int BUY_INDEX = 1;
    private static final int GET_INDEX = 2;
    private static final int START_DATE_INDEX = 3;
    private static final int END_DATE_INDEX = 4;
    private static final int FIELDS_LENGTH = 5;

    public Promotions loadPromotions(String filePath) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            List<Promotion> promotions = lines
                    .skip(1)
                    .map(this::parseLine)
                    .collect(Collectors.toList());
            return new Promotions(promotions);
        } catch (IOException e) {
            throw new IllegalArgumentException(ErrorMessage.FILE_READ_ERROR.getMessage());
        }
    }

    private Promotion parseLine(String line) {
        String[] fields = splitLine(DELIMITER);

        if (fields.length < FIELDS_LENGTH) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }

        String name = fields[NAME_INDEX];
        int buyQuantity = parseInteger(fields[BUY_INDEX]);
        int getQuantity = parseInteger(fields[GET_INDEX]);
        LocalDate startDate = parseDate(fields[START_DATE_INDEX]);
        LocalDate endDate = parseDate(fields[END_DATE_INDEX]);

        return new Promotion(name, buyQuantity, getQuantity, startDate, endDate);
    }

    private static String[] splitLine(String line) {
        try {
            return line.split(DELIMITER);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }

    private static int parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_NUMBER_FORMAT.getMessage());
        }
    }

    private static LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_DATE_FORMAT.getMessage());
        }
    }
}
