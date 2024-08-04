package kr.co.co_working.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class ResponseDto<T> {
    private final String resultCode;
    private final String message;
    private final T data;

    /**
     * ofSuccess : 성공 응답 처리
     **/
    public static <T> ResponseDto<T> ofSuccess() {
        return new ResponseDto<>("SUCCESS", null, null);
    }

    public static <T> ResponseDto<T> ofSuccess(String message) {
        return new ResponseDto<>("SUCCESS", message, null);
    }

    public static <T> ResponseDto<T> ofSuccess(String message, T data) {
        return new ResponseDto<>("SUCCESS", message, data);
    }

    /**
     * ofFail : 실패 응답 처리
     */
    public static <T> ResponseDto<T> ofFail() {
        return new ResponseDto<>("FAIL", null, null);
    }

    public static <T> ResponseDto<T> ofFail(String message) {
        return new ResponseDto<>("FAIL", message, null);
    }

    public static <T> ResponseDto<T> ofFail(String message, T data) {
        return new ResponseDto<>("FAIL", message, data);
    }
}
