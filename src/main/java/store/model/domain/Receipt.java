package store.model.domain;

import java.util.List;

public class Receipt {
    private final List<ReceiptItem> items;
    private final int totalQuantity;
    private final int totalAmount;
    private final int promotionDiscount;
    private final int membershipDiscount;

    public Receipt(List<ReceiptItem> items, int totalQuantity, int totalAmount, int promotionDiscount, int membershipDiscount) {
        this.items = items;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
    }

    public List<ReceiptItem> getItems() {
        return items;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalAmount() {
        return totalAmount - promotionDiscount - membershipDiscount;
    }
}
