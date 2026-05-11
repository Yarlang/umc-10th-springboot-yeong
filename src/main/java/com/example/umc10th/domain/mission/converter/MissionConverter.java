package com.example.umc10th.domain.mission.converter;

import com.example.umc10th.domain.mission.dto.MissionReqDTO;
import com.example.umc10th.domain.mission.dto.MissionResDTO;
import com.example.umc10th.domain.mission.entity.MemberMission;
import com.example.umc10th.domain.mission.entity.Mission;
import com.example.umc10th.domain.store.entity.Store;

import java.util.List;

import static java.time.LocalDate.now;

public class MissionConverter {

    // 멤버 미션 단건 변환 (엔티티 -> DTO)
    public static MissionResDTO.MemberMissionDTO toMemberMissionDTO(MemberMission mm) {
        return new MissionResDTO.MemberMissionDTO(
                mm.getId(),
                mm.getMission().getReward(),
                mm.getStatus(),
                mm.getMission().getContent()
        );
    }

    // 미션 단건 변환 (엔티티 -> DTO)
    public static MissionResDTO.MissionDTO toMissionDTO(Mission m) {
        return new MissionResDTO.MissionDTO(
                m.getId(),
                m.getStore().getName(),
                m.getContent(),
                m.getEndDate(),
                m.getReward()
        );
    }

    // 미션 조회
    public static MissionResDTO.MemberMissionListDTO toMemberMissionListDTO(List<MissionResDTO.MemberMissionDTO> missions, boolean hasNext, Long nextCursor) {
        return MissionResDTO.MemberMissionListDTO.builder()
                .missions(missions)
                .hasNext(hasNext)
                .nextCursor(nextCursor)
                .build();
    }

    // 미션 성공 요청
    public static MissionResDTO.MissionSuccessRequestDTO toMissionSuccessRequestDTO(Long missionId) {
        return MissionResDTO.MissionSuccessRequestDTO.builder()
                .missionId(missionId)
                .build();
    }

    // 미션 성공 승인
    public static MissionResDTO.MissionSuccessConfirmDTO toMissionSuccessConfirmDTO(
            Long missionId
    ) {
        return MissionResDTO.MissionSuccessConfirmDTO.builder()
                .missionId(missionId)
                .completedAt(now())
                .build();
    }

    // 가게 미션 생성
    public static Mission toMission(
            Store store,
            MissionReqDTO.CreateMission dto
    ){
        return Mission.builder()
                .store(store)
                .content(dto.content())
                .reward(dto.reward())
                .endDate(dto.endDate())
                .build();
    }

    // 가게 내 미션 조회
    public static MissionResDTO.GetMission toGetMission(
            Mission mission
    ){
        return MissionResDTO.GetMission.builder()
                .content(mission.getContent())
                .reward(mission.getReward())
                .missionId(mission.getId())
                .build();
    }

    // 페이지네이션 틀 생성
    public static <T> MissionResDTO.Pagination<T> toPagination(
            List<T> data,
            Boolean hasNext,
            String nextCursor,
            Integer pageSize
    ){
        return MissionResDTO.Pagination.<T>builder()
                .data(data)
                .hasNext(hasNext)
                .nextCursor(nextCursor)
                .pageSize(pageSize)
                .build();
    }
}
