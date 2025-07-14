package xyz.samsami.blokey_land.common.exception;

import lombok.Builder;
import xyz.samsami.blokey_land.common.dto.FieldErrorDto;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record CommonExceptionEntity(String code, String message, List<FieldErrorDto> errors, OffsetDateTime timestamp) { }