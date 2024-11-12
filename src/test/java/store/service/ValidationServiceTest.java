package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.model.domain.Order;
import store.model.domain.Product;
import store.model.domain.Products;
import store.model.domain.Promotion;
import store.util.ErrorMessage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ValidationServiceTest {
    private final ValidationService validationService = new ValidationService();
    private final Products products = new Products(List.of(
            new Product("콜라", 1000, 10, Optional.of(new Promotion("탄산2+1", 2, 1, DateTimes.now().minusDays(1), DateTimes.now().plusDays(1)))),
            new Product("사이다", 1000, 10, Optional.empty())
    ));

    @DisplayName("정상적인 상품명과 수량이 입력될 경우 예외가 발생하지 않는다")
    @Test
    void validateInput_successfulCase() {
        assertDoesNotThrow(() -> validationService.validateProductName("[콜라-3]", products.getProductNames()));
    }

    @DisplayName("상품명이 존재하지 않는 경우 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"[없는상품-1]", "[사이다-2]"})
    void validateInput_invalidProductName(String input) {
        assertThatThrownBy(() -> validationService.validateProductName(input, products.getProductNames()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.PRODUCT_NOT_FOUND.getMessage());
    }

    @DisplayName("상품 수량이 1보다 작거나 음수일 경우 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"[콜라--1]"})
    void validateInput_invalidQuantity(String input) {
        assertThatThrownBy(() -> validationService.validateQuantity(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.NON_POSITIVE_QUANTITY.getMessage());
    }

    @DisplayName("올바르지 않은 형식의 입력이 들어오면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"콜라:3"})
    void validateInput_invalidFormat(String input) {
        assertThatThrownBy(() -> validationService.validateItemFormat(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FORMAT.getMessage());
    }

    @DisplayName("재고 수량을 초과하여 주문할 경우 예외가 발생한다")
    @Test
    void validateStockAvailability_exceedsStock() {
        List<Order> orders = List.of(new Order("콜라", 11)); // 콜라 재고는 10이므로 11개 주문 시 예외 발생
        assertThatThrownBy(() -> validationService.validateQuantity(String.valueOf(orders.getFirst().getQuantity())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INSUFFICIENT_STOCK.getMessage());
    }
}
