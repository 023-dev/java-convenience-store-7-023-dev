package store;

import camp.nextstep.edu.missionutils.DateTimes;
import camp.nextstep.edu.missionutils.test.NsTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.model.domain.Order;
import store.model.domain.Product;
import store.model.domain.Products;
import store.model.domain.Promotion;
import store.model.domain.Promotions;
import store.model.domain.Receipt;
import store.service.PaymentService;
import store.service.ValidationService;
import store.util.loader.ProductLoader;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApplicationTest extends NsTest {
    @Test
    void 파일에_있는_상품_목록_출력() {
        assertSimpleTest(() -> {
            run("[물-1]", "N", "N");
            assertThat(output()).contains(
                "- 콜라 1,000원 10개 탄산2+1",
                "- 콜라 1,000원 10개",
                "- 사이다 1,000원 8개 탄산2+1",
                "- 사이다 1,000원 7개",
                "- 오렌지주스 1,800원 9개 MD추천상품",
                "- 오렌지주스 1,800원 재고 없음",
                "- 탄산수 1,200원 5개 탄산2+1",
                "- 탄산수 1,200원 재고 없음",
                "- 물 500원 10개",
                "- 비타민워터 1,500원 6개",
                "- 감자칩 1,500원 5개 반짝할인",
                "- 감자칩 1,500원 5개",
                "- 초코바 1,200원 5개 MD추천상품",
                "- 초코바 1,200원 5개",
                "- 에너지바 2,000원 5개",
                "- 정식도시락 6,400원 8개",
                "- 컵라면 1,700원 1개 MD추천상품",
                "- 컵라면 1,700원 10개"
            );
        });
    }

    @Test
    void 여러_개의_일반_상품_구매() {
        assertSimpleTest(() -> {
            run("[비타민워터-3],[물-2],[정식도시락-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    @Test
    void 기간에_해당하지_않는_프로모션_적용() {
        assertNowTest(() -> {
            run("[감자칩-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈3,000");
        }, LocalDate.of(2024, 2, 1).atStartOfDay());
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() -> {
            runException("[컵라면-12]", "N", "N");
            assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        });
    }

    private final ValidationService validationService = new ValidationService();
    private final PaymentService paymentService = new PaymentService();

    @DisplayName("재고 부족 상황 예외 처리")
    @Test
    void insufficientStockException() {
        Products products = new Products(List.of(
                new Product("콜라", 1000, 5, null)
        ));

        assertThatThrownBy(() -> products.deductStock("콜라", 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다.");
    }

    @DisplayName("상품명이 잘못된 경우 예외 처리")
    @Test
    void invalidProductName() {
        Products products = new Products(List.of(
                new Product("콜라", 1000, 10, null)
        ));

        assertThatThrownBy(() -> products.deductStock("사이다", 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 존재하지 않는 상품입니다.");
    }

    @DisplayName("잘못된 프로모션 설정 예외 처리")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void invalidPromotionConfiguration(int buyQuantity) {
        assertThatThrownBy(() -> new Promotion("탄산2+1", buyQuantity, 1, DateTimes.now(), DateTimes.now().plusDays(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 구매 수량 조건이 잘못되었습니다.");
    }

    @DisplayName("프로모션 기간이 아닐 경우 무료 증정 불가")
    @Test
    void promotionOutsideValidPeriod() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, DateTimes.now().minusDays(5), DateTimes.now().minusDays(1));
        assertThat(promotion.isInPromotionPeriod()).isFalse();
        assertThat(promotion.getFreeQuantity(5)).isZero();
    }

    @DisplayName("올바른 상품 및 프로모션 정보 로드")
    @Test
    void loadProductAndPromotions() {
        ProductLoader productLoader = new ProductLoader(new Promotions(List.of()));
        Products products = productLoader.loadProducts();

        assertThat(products.getProductNames()).contains("콜라", "사이다");
    }

    @DisplayName("멤버십 할인 한도 초과 예외")
    @ParameterizedTest
    @ValueSource(ints = {25000, 30000})
    void membershipDiscountLimit(int nonPromotionAmount) {
        int discount = PaymentService.membershipDiscount(nonPromotionAmount);
        assertThat(discount).isLessThanOrEqualTo(8000);
    }

    @DisplayName("정상 결제 프로세스")
    @Test
    void checkoutProcess() {
        Products products = new Products(List.of(
                new Product("콜라", 1000, 10, Optional.of(
                        new Promotion("탄산2+1", 2, 1, DateTimes.now().minusDays(1), DateTimes.now().plusDays(1))))
        ));

        List<Order> orders = List.of(new Order("콜라", 5));
        Receipt receipt = paymentService.checkout(products, orders, true);

        assertThat(receipt.getFinalAmount()).isEqualTo(4000); // 예측 값
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
