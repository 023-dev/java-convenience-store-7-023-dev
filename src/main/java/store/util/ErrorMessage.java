package store.util;

public enum ErrorMessage {

    // 파일 입출력 관련 에러 메시지
    FILE_NOT_FOUNT_ERROR("파일을 찾을 수 없습니다."),
    FILE_READ_ERROR("파일을 읽는 도중 오류가 발생했습니다."),
    INVALID_FILE_FORMAT("파일 형식이 잘못되었습니다."),
    INVALID_NUMBER_FORMAT("숫자 형식이 잘못되었습니다."),
    INVALID_DATE_FORMAT("날짜 형식이 잘못되었습니다."),

    // 상품 관련 에러 메시지
    PRODUCT_NOT_FOUND("존재하지 않는 상품입니다."),
    INSUFFICIENT_STOCK("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INVALID_PRICE_OR_STOCK("가격과 재고는 0 이상이어야 합니다."),
    ORDER_CANCELED("구매 취소되었습니다. 다시 상품을 입력해 주세요."),

    // 프로모션 관련 에러 메시지
    INVALID_PROMOTION_CONDITION("프로모션 조건이 잘못되었습니다."),
    PROMOTION_PERIOD_EXPIRED("프로모션 기간이 아닙니다."),

    // 입력 관련 에러 메시지
    INVALID_INPUT_FORMAT("입력 형식이 잘못되었습니다."),
    INVALID_QUANTITY("수량은 정수여야 합니다."),
    INVALID_FORMAT("[상품명-수량]형식이어야 합니다."),
    NON_POSITIVE_QUANTITY("수량은 양수여야 합니다."),
    INVALID_FORMAT_BRACKETS("각 항목은 대괄호로 둘러싸여야 합니다."),
    EMPTY_INPUT("입력이 비어있거나 null값 입니다."),
    INVALID_Y_OR_N("입력은 Y 또는 N만 가능합니다."),

    // 구매 및 증정 수량 검증 관련 에러 메시지
    INVALID_BUY_QUANTITY("구매 수량 조건이 잘못되었습니다."),
    INVALID_FREE_QUANTITY("무료 증정 수량은 1개여야 합니다.");

    private static final String ERROR_MESSAGE_PREFIX = "[ERROR] ";
    private final String message;

    ErrorMessage(String message) {
        this.message = ERROR_MESSAGE_PREFIX + message;
    }

    public String getMessage() {
        return message;
    }
}
