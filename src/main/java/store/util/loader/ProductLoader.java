package store.util.loader;

import static store.common.Constants.DELIMITER;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.model.domain.Product;
import store.model.domain.Products;
import store.model.domain.Promotion;
import store.model.domain.Promotions;
import store.util.ErrorMessage;

public class ProductLoader {
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.txt";
    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_NAME_INDEX = 3;
    private static final int FIELDS_LENGTH = 4;

    private final Promotions promotions;

    public ProductLoader(Promotions promotions) {
        this.promotions = promotions;
    }

    public Products loadPromotions() {
        try (Stream<String> lines = Files.lines(Paths.get(PRODUCT_FILE_PATH))) {
            List<Product> products = lines
                    .skip(1)
                    .map(this::parseLine)
                    .collect(Collectors.toList());
            return new Products(products);
        } catch (IOException e) {
            throw new IllegalArgumentException(ErrorMessage.FILE_READ_ERROR.getMessage());
        }
    }

    private Product parseLine(String line) {
        String[] fields = splitLine(line);

        String name = fields[NAME_INDEX];
        int price = parseInteger(fields[PRICE_INDEX]);
        int quantity = parseInteger(fields[QUANTITY_INDEX]);
        String promotionName = fields[PROMOTION_NAME_INDEX];

        Optional<Promotion> promotion = promotions.findByName(promotionName);
        return new Product(name, price, quantity, promotion);
    }

    private static String[] splitLine(String line) {
        try {
            String[] fields = line.split(DELIMITER);
            validateFiledsLength(fields);
            return fields;
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

    private static void validateFiledsLength(String[] fields) {
        if (fields.length < FIELDS_LENGTH) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }
}
