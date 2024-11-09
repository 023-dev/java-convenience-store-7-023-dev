package store.util.reader;

import store.model.dto.ProductDto;

public class ProductFileReader extends AbstractFileReader<ProductDto> {

    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;

    @Override
    protected ProductDto parseLine(String line) {
        String[] fields = line.split(DELIMITER);

        String name = fields[NAME_INDEX];
        int price = Integer.parseInt(fields[PRICE_INDEX]);
        int quantity = Integer.parseInt(fields[QUANTITY_INDEX]);
        String promotion = fields[PROMOTION_INDEX];

        return new ProductDto(name, price, quantity, promotion);
    }

}
