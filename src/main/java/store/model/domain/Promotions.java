package store.model.domain;

import java.util.List;
import java.util.Optional;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Optional<Promotion> findByName(String name) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
