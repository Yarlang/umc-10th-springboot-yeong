package com.example.umc10th.domain.mission.dto;

import com.example.umc10th.domain.mission.enums.Status;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public class MissionResDTO {
    // 미션 단일 조회
    @Builder
    public record MissionDTO(
            Long missionId,
            String storeName,
            String content,
            LocalDate endDate,
            Integer reward
    ) {}

    // 멤버 미션 목록 조회
    @Builder
    public record MemberMissionListDTO(
            List<MemberMissionDTO> missions,
            Boolean hasNext,
            Long nextCursor
    ) {}

    // 단일 멤버 미션
    @Builder
    public record MemberMissionDTO(
            Long memberMissionId,
            Integer reward,
            Status status,
            String content
    ){}

    // 미션 성공 요청
    @Builder
    public record MissionSuccessRequestDTO(
            Long missionId
    ){}

    // 미션 성공 승인
    @Builder
    public record MissionSuccessConfirmDTO(
            Long missionId,
            LocalDate completedAt
    ){}

    // 가게 내 미션 조회
    @Builder
    public record GetMission(
            Long missionId,
            Integer reward,
            String content
    ){}

    // 페이지네이션 틀
    @Builder
    public record Pagination<T>(
            List<T> data,
            Boolean hasNext,
            String nextCursor,
            Integer pageSize
    ){}
}
