package store.util.loader;

import java.util.List;
import java.util.stream.Collectors;
import store.model.domain.Product;
import store.model.domain.ProductInventory;
import store.model.domain.Promotion;
import store.model.dto.ProductDto;
import store.model.dto.PromotionDto;
import store.util.reader.AbstractFileReader;
import store.util.reader.ProductFileReader;
import store.util.reader.PromotionFileReader;

public class InventoryLoader {

    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.txt";
    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.txt";

    private final AbstractFileReader<ProductDto> productFileReader;
    private final AbstractFileReader<PromotionDto> promotionFileReader;

    public InventoryLoader(ProductFileReader productFileReader, PromotionFileReader promotionFileReader) {
        this.productFileReader = productFileReader;
        this.promotionFileReader = promotionFileReader;
    }

    public ProductInventory loadInventory() {
        List<PromotionDto> promotionDtos = loadPromotionsDtos();
        List<Product> products = loadProducts(promotionDtos);
        return new ProductInventory(products);
    }

    private List<ProductDto> loadProductDtos() {
        return productFileReader.readFile(PRODUCT_FILE_PATH);
    }

    private List<PromotionDto> loadPromotionsDtos() {
        return promotionFileReader.readFile(PROMOTION_FILE_PATH);
    }

    private Promotion convertToPromotion(PromotionDto promotionDto) {
        return new Promotion(
                promotionDto.name(),
                promotionDto.buyQuantity(),
                promotionDto.freeQuantity(),
                promotionDto.startDate(),
                promotionDto.endDate()
        );
    }

    private List<Product> loadProducts(List<PromotionDto> promotionDtos) {
        return loadProductDtos().stream()
                .map(productDto -> createProduct(productDto, promotionDtos))
                .collect(Collectors.toList());
    }

    private Product createProduct(ProductDto productDto, List<PromotionDto> promotionDtos) {
        Promotion matchingPromotion = findMatchingPromotion(promotionDtos, productDto.promotionName());
        return new Product(
                productDto.name(),
                productDto.price(),
                productDto.quantity(),
                matchingPromotion
        );
    }

    private Promotion findMatchingPromotion(List<PromotionDto> promotionDtos, String promotionName) {
        return promotionDtos.stream()
                .filter(promotion -> promotion.name().equals(promotionName))
                .map(this::convertToPromotion)
                .findFirst()
                .orElse(null);
    }
}
