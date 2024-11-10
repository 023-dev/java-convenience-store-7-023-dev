package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String INPUT_ITEM_QUANTITY = "구매할 상품명과 수량을 입력해 주세요. (예: [콜라-10],[사이다-3])";

    public String readItemInput() {
        System.out.println(INPUT_ITEM_QUANTITY);
        return Console.readLine();
    }

    public static boolean readMembershipDiscount() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return "Y".equalsIgnoreCase(Console.readLine());
    }
}
