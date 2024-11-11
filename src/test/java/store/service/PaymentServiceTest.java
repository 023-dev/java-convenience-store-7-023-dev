package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentServiceTest {
    private PaymentService paymentService;
    private Products products;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService();

        // 프로모션이 있는 상품과 일반 상품을 준비합니다.
        Product promoProduct = new Product("콜라", 1000, 10,
                Optional.of(new Promotion("탄산2+1", 2, 1,
                        LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1))));
        Product regularProduct = new Product("에너지바", 2000, 5, Optional.empty());

        products = new Products(List.of(promoProduct, regularProduct));
    }

    @DisplayName("프로모션 상품을 구매 시 행사할인 및 증정 상품이 적용되는지 테스트")
    @Test
    void checkout_withPromotion() {
        List<Order> orders = List.of(new Order("콜라", 3));
        Receipt receipt = paymentService.checkout(products, orders, false);

        assertThat(receipt.getTotalAmount()).isEqualTo(3000);
        assertThat(receipt.getPromotionDiscount()).isEqualTo(1000); // 1개 무료 증정
        assertThat(receipt.getMembershipDiscount()).isEqualTo(0);
        assertThat(receipt.getFinalAmount()).isEqualTo(2000);
    }

    @DisplayName("프로모션이 없는 상품을 구매 시 멤버십 할인이 적용되는지 테스트")
    @Test
    void checkout_withMembershipDiscount() {
        List<Order> orders = List.of(new Order("에너지바", 5));
        Receipt receipt = paymentService.checkout(products, orders, true);

        assertThat(receipt.getTotalAmount()).isEqualTo(10000);
        assertThat(receipt.getPromotionDiscount()).isEqualTo(0);
        assertThat(receipt.getMembershipDiscount()).isEqualTo(3000); // 멤버십 할인 적용
        assertThat(receipt.getFinalAmount()).isEqualTo(7000);
    }

    @DisplayName("재고를 초과하여 주문 시 예외 발생")
    @Test
    void checkout_exceedsStock() {
        List<Order> orders = List.of(new Order("콜라", 15));

        assertThatThrownBy(() -> paymentService.checkout(products, orders, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다.");
    }

    @DisplayName("프로모션과 멤버십 할인이 동시에 적용되는 시나리오")
    @Test
    void checkout_withPromotionAndMembership() {
        List<Order> orders = List.of(new Order("콜라", 3), new Order("에너지바", 2));
        Receipt receipt = paymentService.checkout(products, orders, true);

        assertThat(receipt.getTotalAmount()).isEqualTo(5000); // 3000 + 4000
        assertThat(receipt.getPromotionDiscount()).isEqualTo(1000); // 콜라 1개 무료 증정
        assertThat(receipt.getMembershipDiscount()).isEqualTo(600); // 에너지바 2개에 대해 30% 할인
        assertThat(receipt.getFinalAmount()).isEqualTo(3400);
    }
}
