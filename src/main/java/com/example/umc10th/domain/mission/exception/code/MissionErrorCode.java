package com.example.umc10th.domain.mission.exception.code;

import com.example.umc10th.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MissionErrorCode implements BaseErrorCode {
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "MISSION404_1", "해당 미션을 찾을 수 없습니다."),
    MISSION_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "MISSION400_1", "이미 처리된 미션 입니다."),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "MISSION400_1", "해당 상태는 유효하지 않습니다."),
    QUERY_INVALID(HttpStatus.BAD_REQUEST, "MISSION400_2", "해당 쿼리는 유효하지 않습니다")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
