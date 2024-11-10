package store.model.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.model.dto.ProductDto;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public void addPromotion(Promotion promotion) {
        promotions.add(promotion);
    }

    public Optional<Promotion> findByName(String name) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
