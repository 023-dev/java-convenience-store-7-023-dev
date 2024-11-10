package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String ITEM_QUANTITY_PROMPT = "구매할 상품명과 수량을 입력해 주세요. (예: [콜라-10],[사이다-3])";
    private static final String PROMOTION_PROMPT = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String PROMOTION_SHORTAGE_PROMPT = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String MEMBERSHIP_PROMPT = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String ADDITIONAL_PURCHASE_PROMPT = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public String readItemInput() {
        System.out.println(ITEM_QUANTITY_PROMPT);
        return Console.readLine();
    }

    public String readPromotion(String productName) {
        System.out.println((PROMOTION_PROMPT).formatted(productName));
        return Console.readLine();
    }

    public String readPromotionShortage(String productName, int shortageQuantity) {
        System.out.println((PROMOTION_SHORTAGE_PROMPT).formatted(productName, shortageQuantity));
        return Console.readLine();
    }

    public String readMembership() {
        System.out.println(MEMBERSHIP_PROMPT);
        return Console.readLine();
    }

    public String readAdditionalPurchase() {
        System.out.println(ADDITIONAL_PURCHASE_PROMPT);
        return Console.readLine();
    }
}
