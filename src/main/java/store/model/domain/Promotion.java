package store.model.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Promotion {
    private final String name;
    private final int buyQuantity;
    private final int freeQuantity;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Promotion(String name, int buyQuantity, int freeQuantity, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;

        validate();
    }

    private void validate() {
        validateBuyQuantity();
        validateFreeQuantity();
    }

    private void validateBuyQuantity() {
        if (buyQuantity <= 0) {
            throw new IllegalArgumentException("[ERROR] 구매 수량 조건이 잘못되었습니다.");
        }
    }

    private void validateFreeQuantity() {
        if (freeQuantity != 1) {
            throw new IllegalArgumentException("[ERROR] 무료 증정 수량은 1개여야 합니다.");
        }
    }

    private boolean isInPromotionPeriod() {
        LocalDateTime today = DateTimes.now();
        return (today.isAfter(startDate) || today.isEqual(startDate)) &&
                (today.isBefore(endDate) || today.isEqual(endDate));
    }

    public int calculateTotalQuantity(int purchasedQuantity) {
        if (!isInPromotionPeriod()) {
            return purchasedQuantity;
        }
        int totalFreeItems = (purchasedQuantity / buyQuantity) * freeQuantity;
        return purchasedQuantity + totalFreeItems;
    }

    public String getName() {
        return name;
    }

}
