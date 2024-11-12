package store.model.domain;

import static store.util.ErrorMessage.INVALID_BUY_QUANTITY;
import static store.util.ErrorMessage.INVALID_FREE_QUANTITY;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;

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
            throw new IllegalArgumentException(INVALID_BUY_QUANTITY.getMessage());
        }
    }

    private void validateFreeQuantity() {
        if (freeQuantity != 1) {
            throw new IllegalArgumentException(INVALID_FREE_QUANTITY.getMessage());
        }
    }

    public boolean isInPromotionPeriod() {
        LocalDateTime today = DateTimes.now();
        return (today.isAfter(startDate) || today.isEqual(startDate)) &&
                (today.isBefore(endDate) || today.isEqual(endDate));
    }

    public boolean isAdditionalFreeItem(int orderedQuantity) {
        return isInPromotionPeriod() && orderedQuantity < buyQuantity;
    }

    public int getPaidQuantity(int orderedQuantity) {
        if (!isInPromotionPeriod()) return orderedQuantity;
        int groups = orderedQuantity / (buyQuantity + freeQuantity);
        int remaining = orderedQuantity % (buyQuantity + freeQuantity);
        return (groups * buyQuantity) + Math.min(remaining, buyQuantity);
    }

    public int getFreeQuantity(int orderedQuantity) {
        if (!isInPromotionPeriod()) return 0;
        int groups = orderedQuantity / (buyQuantity + freeQuantity);
        return groups * freeQuantity;
    }

    public int getTotalQuantity(int purchasedQuantity) {
        if (!isInPromotionPeriod()) return purchasedQuantity;
        int totalFreeItems = (purchasedQuantity / buyQuantity) * freeQuantity;
        return purchasedQuantity + totalFreeItems;
    }

    public String getName() {
        return name;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }
}
