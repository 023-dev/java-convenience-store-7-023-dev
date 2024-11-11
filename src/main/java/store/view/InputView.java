package store.view;

import static store.common.constant.Constants.NEW_LINE;
import static store.common.constant.Constants.Y;
import static store.common.constant.Constants.Y_OR_N;
import static store.common.constant.Messages.PROMPT_CONTINUE_SHOPPING;
import static store.common.constant.Messages.PROMPT_ITEM_INPUT;
import static store.common.constant.Messages.PROMPT_MEMBERSHIP_DISCOUNT;
import static store.common.constant.Messages.PROMPT_PROMOTION;
import static store.common.constant.Messages.PROMPT_PROMOTION_SHORTAGE;
import static store.util.ErrorMessage.INVALID_Y_OR_N;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    public String readItem() {
        System.out.println(NEW_LINE+PROMPT_ITEM_INPUT);
        return Console.readLine();
    }

    public boolean readPromotion(String productName) {
        System.out.println(NEW_LINE + (PROMPT_PROMOTION).formatted(productName));
        String input = Console.readLine().toUpperCase();
        validateYesOrNo(input);
        return input.equalsIgnoreCase(Y);
    }

    public boolean readPromotionShortage(String productName, int shortageQuantity) {
        System.out.println(NEW_LINE + (PROMPT_PROMOTION_SHORTAGE).formatted(productName, shortageQuantity));
        String input = Console.readLine();
        validateYesOrNo(input);
        return input.equalsIgnoreCase(Y);
    }

    public boolean readMembership() {
        System.out.println(NEW_LINE + PROMPT_MEMBERSHIP_DISCOUNT);
        String input = Console.readLine();
        validateYesOrNo(input);
        return input.equalsIgnoreCase(Y);
    }

    public boolean readContinue() {
        System.out.println(NEW_LINE + PROMPT_CONTINUE_SHOPPING);
        String input = Console.readLine();
        validateYesOrNo(input);
        return input.equalsIgnoreCase(Y);
    }

    public void validateYesOrNo (String input) {
        if (!Y_OR_N.contains(input)) {
            throw new IllegalArgumentException(INVALID_Y_OR_N.getMessage());
        }
    }
}
