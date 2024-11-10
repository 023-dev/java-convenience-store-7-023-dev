package store.model.domain;

import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final int buyQuantity;
    private final int freeQuantity;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, int buyQuantity, int freeQuantity, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private boolean isInPromotionPeriod() {
        LocalDate today = LocalDate.today();
        return (today.isAfter(startDate) || today.isEqual(startDate)) &&
                (today.isBefore(endDate) || today.isEqual(endDate));
    }
}
