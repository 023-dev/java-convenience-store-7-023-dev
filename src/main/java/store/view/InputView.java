package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String ITEM_QUANTITY_PROMPT = "구매할 상품명과 수량을 입력해 주세요. (예: [콜라-10],[사이다-3])";
    private static final String PROMOTION_PROMPT = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";

    public String readItemInput() {
        System.out.println(ITEM_QUANTITY_PROMPT);
        return Console.readLine();
    }

    public String readPromotionOption(String productName) {
        System.out.printf((PROMOTION_PROMPT) + "\n", productName);
        return Console.readLine();
    }
}
