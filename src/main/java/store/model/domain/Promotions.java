package store.model.domain;

import java.util.ArrayList;
import java.util.List;
import store.model.dto.ProductDto;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public void addPromotion(Promotion promotion) {
        promotions.add(promotion);
    }
}
