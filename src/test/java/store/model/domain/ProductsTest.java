package store.model.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.util.ErrorMessage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductsTest {

    @DisplayName("이름으로 상품 찾기 - 상품이 존재하는 경우")
    @Test
    void findByName_existingProduct() {
        Products products = new Products(List.of(new Product("콜라", 1000, 10, Optional.empty())));
        assertThat(products.findByName("콜라")).isPresent();
    }

    @DisplayName("이름으로 상품 찾기 - 상품이 존재하지 않는 경우 예외 발생")
    @Test
    void findByName_nonExistingProduct() {
        Products products = new Products(List.of(new Product("콜라", 1000, 10, Optional.empty())));
        assertThat(products.findByName("사이다")).isNotPresent();
    }

    @DisplayName("프로모션 우선 순위로 상품 찾기")
    @Test
    void findPrioritizedProduct() {
        Product promoProduct = new Product("콜라", 1000, 10, Optional.of(new Promotion("탄산2+1", 2, 1, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1))));
        Product regularProduct = new Product("콜라", 1000, 10, Optional.empty());
        Products products = new Products(List.of(promoProduct, regularProduct));

        Product prioritizedProduct = products.findPrioritizedProduct("콜라").orElseThrow();
        assertThat(prioritizedProduct.hasPromotion()).isTrue();
    }

    @DisplayName("재고가 부족한 경우 예외 발생")
    @Test
    void deductStock_insufficientStock() {
        Product product = new Product("콜라", 1000, 5, Optional.empty());
        Products products = new Products(List.of(product));

        assertThatThrownBy(() -> products.deductStock("콜라", 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INSUFFICIENT_STOCK.getMessage());
    }
}
