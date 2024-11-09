package store.model.dto;

import java.time.LocalDate;

public record PromotionDto(String name, int buyQuantity, int freeQuantity, LocalDate startDate, LocalDate endDate) { }
