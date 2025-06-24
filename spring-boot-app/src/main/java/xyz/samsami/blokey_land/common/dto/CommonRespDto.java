package xyz.samsami.blokey_land.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.samsami.blokey_land.common.type.ResultType;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Getter
@Builder
@RequiredArgsConstructor(staticName = "of")
public class CommonRespDto<T> {
    private final ResultType resultType;
    private final String message;
    private final T data;
    private final OffsetDateTime timestamp;

    public static <T> CommonRespDto<T> of(ResultType resultType, String message, T data) {
        return CommonRespDto.<T>builder()
            .resultType(resultType)
            .message(message)
            .data(data)
            .timestamp(OffsetDateTime.now(ZoneId.of("Asia/Seoul")))
            .build();
    }
}
