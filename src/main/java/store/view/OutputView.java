package store.view;


import static store.common.constant.Constants.EMPTY;
import static store.common.constant.Constants.NEW_LINE;
import static store.common.constant.Messages.FREE_ITEM_HEADER;
import static store.common.constant.Messages.ITEM_HEADER;
import static store.common.constant.Messages.NO_STOCK_MESSAGE;
import static store.common.constant.Messages.PRODUCT_LIST_BODY;
import static store.common.constant.Messages.PRODUCT_LIST_HEADER;
import static store.common.constant.Messages.RECEIPT_AMOUNT_HEADER;
import static store.common.constant.Messages.RECEIPT_FINAL_AMOUNT;
import static store.common.constant.Messages.RECEIPT_HEADER;
import static store.common.constant.Messages.RECEIPT_MEMBERSHIP_DISCOUNT;
import static store.common.constant.Messages.RECEIPT_PROMOTION_DISCOUNT;
import static store.common.constant.Messages.RECEIPT_TOTAL_AMOUNT;
import static store.common.constant.Messages.STOCK_MESSAGE;
import static store.common.constant.Messages.WELCOME_MESSAGE;

import store.model.domain.Product;
import store.model.domain.Products;
import store.model.domain.Promotion;
import store.model.domain.Receipt;
import store.model.domain.ReceiptItem;

public class OutputView {

    private String formatPrice(int price) {
        return String.format("%,d", price);
    }

    public void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    public void printProducts(Products products) {
        System.out.println(PRODUCT_LIST_HEADER + NEW_LINE);
        String stockStatus;
        for (Product product : products.getProducts()) {
            stockStatus = isNoStock(product.getStock());
            String promotionName = product.getPromotion().map(Promotion::getName).orElse(EMPTY);
            System.out.println(PRODUCT_LIST_BODY.formatted(product.getName(), formatPrice(product.getPrice()), stockStatus, promotionName).trim());
            isOnlyPromotion(products, product);
        }
    }

    private String isNoStock(int stock) {
        String stockStatus = NO_STOCK_MESSAGE;
        if (stock > 0) {
            stockStatus = STOCK_MESSAGE.formatted(stock);
        }
        return stockStatus;
    }

    private void isOnlyPromotion(Products products, Product product) {
        if (product.hasPromotion() && products.findProductByName(product.getName(), false).isEmpty()) {
            System.out.println(PRODUCT_LIST_BODY.formatted(product.getName(), formatPrice(product.getPrice()), NO_STOCK_MESSAGE, EMPTY).trim());
        }
    }

    public void printErrorMessage(String message) {
        System.out.println(message);
    }

    public void printReceipt(Receipt receipt) {
        System.out.println(RECEIPT_HEADER);
        printItems(receipt);
        printFreeItems(receipt);
        printSummary(receipt);
    }

    private void printItems(Receipt receipt) {
        System.out.println(ITEM_HEADER);
        for (ReceiptItem item : receipt.getItems()) {
            if (!item.isFreeItem()) {
                System.out.printf("%s\t\t%d\t%s%n", item.getName(), item.getQuantity(), formatPrice(item.getPrice()));
            }
        }
    }

    private void printFreeItems(Receipt receipt) {
        System.out.println(FREE_ITEM_HEADER);
        for (ReceiptItem item : receipt.getItems()) {
            if (item.isFreeItem()) {
                System.out.printf("%s\t\t%d%n", item.getName(), item.getQuantity());
            }
        }
    }

    private void printSummary(Receipt receipt) {
        System.out.println(RECEIPT_AMOUNT_HEADER);
        System.out.printf("%s\t%d\t%s%n", RECEIPT_TOTAL_AMOUNT, receipt.getTotalQuantity(), formatPrice(receipt.getTotalAmount()));
        System.out.printf("%s\t\t\t-%s%n", RECEIPT_PROMOTION_DISCOUNT, formatPrice(receipt.getPromotionDiscount()));
        System.out.printf("%s\t\t\t-%s%n", RECEIPT_MEMBERSHIP_DISCOUNT, formatPrice(receipt.getMembershipDiscount()));
        System.out.printf("%s\t\t\t%s%n", RECEIPT_FINAL_AMOUNT, formatPrice(receipt.getFinalAmount()));
    }
}