package store.service;

import store.model.domain.Order;
import store.model.domain.Product;
import store.model.domain.Products;
import store.model.domain.Receipt;
import store.model.domain.ReceiptItem;

import java.util.ArrayList;
import java.util.List;

public class PaymentService {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

    public Receipt checkout(Products products, List<Order> orders, boolean isMember) {
        List<ReceiptItem> items = new ArrayList<>();
        int totalAmount = calculateTotalAmount(orders, products, items);
        int promotionAmount = promotionDiscount(orders, products);
        int nonPromotionAmount = nonPromotionAmount(orders, products);
        int membershipAmount = 0;
        int totalQuantity = totalQuantity(items);
        if (isMember) {
            membershipAmount = membershipDiscount(nonPromotionAmount);
        }
        return new Receipt(items, totalQuantity, totalAmount, promotionAmount, membershipAmount);
    }

    private int calculateTotalAmount(List<Order> orders, Products products, List<ReceiptItem> items) {
        int totalAmount = 0;
        for (Order order : orders) {
            totalAmount += processOrder(order, products, items);
        }
        return totalAmount;
    }

    private int promotionDiscount(List<Order> orders, Products products) {
        int promotionDiscount = 0;
        for (Order order : orders) {
            promotionDiscount += promotionDiscountOrder(order, products);
        }
        return promotionDiscount;
    }

    private int nonPromotionAmount(List<Order> orders, Products products) {
        int nonPromotionAmount = 0;
        for (Order order : orders) {
            if (!isPromotionApplied(order, products)) {
                nonPromotionAmount += orderTotal(order, products);
            }
        }
        return nonPromotionAmount;
    }

    private int totalQuantity(List<ReceiptItem> items) {
        return items.stream()
                .filter(item -> !item.isFreeItem())
                .mapToInt(ReceiptItem::getQuantity)
                .sum();
    }

    private int processOrder(Order order, Products products, List<ReceiptItem> items) {
        int orderQuantity = order.getQuantity();
        products.deductStock(order.getProductName(), orderQuantity);
        Product product = getProductByName(order.getProductName(), products);
        int actualQuantity = product.getActualQuantity(orderQuantity);
        int freeQuantity = product.getFreeQuantity(orderQuantity);
        addReceiptItems(items, product, orderQuantity, actualQuantity, freeQuantity);
        return product.getTotalPrice(orderQuantity);
    }

    private Product getProductByName(String productName, Products products) {
        return products.findProductByName(productName, true)
                .or(() -> products.findProductByName(productName, false))
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다."));
    }

    private int orderTotal(Order order, Products products) {
        Product product = getProductByName(order.getProductName(), products);
        return product.getTotalPrice(order.getQuantity());
    }

    private int promotionDiscountOrder(Order order, Products products) {
        Product product = getProductByName(order.getProductName(), products);
        int freeQuantity = product.getFreeQuantity(order.getQuantity());
        return product.getTotalPrice(freeQuantity);
    }

    private boolean isPromotionApplied(Order order, Products products) {
        Product product = getProductByName(order.getProductName(), products);
        return product.hasPromotion();
    }

    public static int membershipDiscount(int amount) {
        int discount = (int) (amount * MEMBERSHIP_DISCOUNT_RATE);
        return Math.min(discount, MAX_MEMBERSHIP_DISCOUNT);
    }

    private void addReceiptItems(List<ReceiptItem> items, Product product, int orderQuantity, int actualQuantity, int freeQuantity) {
        items.add(new ReceiptItem(product.getName(), orderQuantity, product.getTotalPrice(orderQuantity), false));
        if (freeQuantity > 0) {
            items.add(new ReceiptItem(product.getName(), freeQuantity, 0, true));
        }
    }
}
