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
    INSUFFICIENT_STOCK("재고가 부족하여 구매할 수 없습니다."),
    INVALID_PRICE_OR_STOCK("가격과 재고는 0 이상이어야 합니다."),

    // 프로모션 관련 에러 메시지
    INVALID_PROMOTION_CONDITION("프로모션 조건이 잘못되었습니다."),
    PROMOTION_PERIOD_EXPIRED("프로모션 기간이 아닙니다."),

    // 입력 관련 에러 메시지
    INVALID_INPUT_FORMAT("입력 형식이 잘못되었습니다."),
    EMPTY_INPUT("입력이 비어 있습니다.");

    private static final String ERROR_MESSAGE_PREFIX = "[ERROR] ";
    private final String message;

    ErrorMessage(String message) {
        this.message = ERROR_MESSAGE_PREFIX + message;
    }

    public String getMessage() {
        return message;
    }
}
