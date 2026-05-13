package com.example.umc10th.domain.review.exception.code;

import com.example.umc10th.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode {
    QUERY_INVALID(HttpStatus.BAD_REQUEST, "REVIEW400_1", "해당 쿼리는 유효하지 않습니다"),
    CURSOR_INVALID(HttpStatus.BAD_REQUEST, "REVIEW400_2", "커서 형식이 올바르지 않습니다"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
