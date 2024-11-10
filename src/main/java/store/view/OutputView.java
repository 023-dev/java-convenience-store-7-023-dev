package store.view;


import static store.common.Constants.EMPTY;

import java.util.List;
import store.model.domain.Product;
import store.model.domain.Products;
import store.model.domain.Promotion;
import store.model.dto.ProductDto;

public class OutputView {
    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String PRODUCT_LIST_HEADER = "현재 보유하고 있는 상품입니다.";
    private static final String PRODUCT_LIST_BODY = "- %s %d원 %s %s";
    private static final String STOCK_FORMAT = "%d개";
    private static final String NO_STOCK_MESSAGE = "재고 없음";

    public static void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }
}