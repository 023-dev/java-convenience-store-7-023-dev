package store.util;

public enum ErrorMessage {

    // 파일 입출력 관련 에러 메시지
    FILE_READ_ERROR("파일을 읽는 도중 오류가 발생했습니다."),
    INVALID_FILE_FORMAT("파일 형식이 잘못되었습니다."),
    INVALID_NUMBER_FORMAT("숫자 형식이 잘못되었습니다."),
    INVALID_DATE_FORMAT("날짜 형식이 잘못되었습니다.");

    private static final String ERROR_MESSAGE_PREFIX = "[ERROR] ";
    private final String message;

    ErrorMessage(String message) {
        this.message = ERROR_MESSAGE_PREFIX + message;
    }

    public String getMessage() {
        return message;
    }
}
