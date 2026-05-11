package com.example.umc10th.domain.mission.service;

import com.example.umc10th.domain.mission.converter.MissionConverter;
import com.example.umc10th.domain.mission.dto.MissionReqDTO;
import com.example.umc10th.domain.mission.dto.MissionResDTO;
import com.example.umc10th.domain.mission.entity.MemberMission;
import com.example.umc10th.domain.mission.entity.Mission;
import com.example.umc10th.domain.mission.enums.Status;
import com.example.umc10th.domain.mission.exception.MissionException;
import com.example.umc10th.domain.mission.exception.code.MissionErrorCode;
import com.example.umc10th.domain.mission.repository.MemberMissionRepository;
import com.example.umc10th.domain.mission.repository.MissionRepository;
import com.example.umc10th.domain.store.entity.Store;
import com.example.umc10th.domain.store.exception.StoreException;
import com.example.umc10th.domain.store.exception.code.StoreErrorCode;
import com.example.umc10th.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionService {

    private final MemberMissionRepository memberMissionRepository;
    private final StoreRepository storeRepository;
    private final MissionRepository missionRepository;

    // 미션 조회
    public MissionResDTO.MemberMissionListDTO getMissionList(Long memberId, String statusStr, Long cursorMissionId, Integer size) {
        // 상태 필터
        Status status = switch (statusStr) {
            case "ONGOING" -> Status.NONE;
            case "COMPLETED" -> Status.SUCCESS;
            case "FAILED" -> Status.FAIL;
            default -> throw new MissionException(MissionErrorCode.INVALID_STATUS);
        };

        int requestSize = size;
        List<MemberMission> entities = memberMissionRepository.findMissions(
                memberId,
                status,
                cursorMissionId, PageRequest.of(0, requestSize + 1));

        boolean hasNext = entities.size() > requestSize;

        List<MemberMission> page = hasNext ? entities.subList(0, requestSize) : entities;

        List<MissionResDTO.MemberMissionDTO> missions = page.stream()
                .map(MissionConverter::toMemberMissionDTO)
                .toList();

        Long nextCursor = null;
        if (hasNext && !page.isEmpty()) {
            nextCursor = page.get(page.size() - 1).getId();
        }

        return MissionConverter.toMemberMissionListDTO(missions, hasNext, nextCursor);
    }

    // 미션 성공 요청
    public MissionResDTO.MissionSuccessRequestDTO getMissionSuccessRequest(Long memberMissionId) {
        MemberMission mm = memberMissionRepository.findById(memberMissionId)
                .orElseThrow(() -> new MissionException(MissionErrorCode.MISSION_NOT_FOUND));

        if (mm.getStatus()!= Status.NONE) {throw new MissionException(MissionErrorCode.MISSION_ALREADY_PROCESSED);}

        return MissionConverter.toMissionSuccessRequestDTO(memberMissionId);
    }

    // 미션 성공 승인
    @Transactional
    public MissionResDTO.MissionSuccessConfirmDTO getMissionSuccessConfirm(Long memberMissionId) {
        MemberMission mm = memberMissionRepository.findById(memberMissionId)
                .orElseThrow(() -> new MissionException(MissionErrorCode.MISSION_NOT_FOUND));

        if (mm.getStatus()!= Status.NONE) {throw new MissionException(MissionErrorCode.MISSION_ALREADY_PROCESSED);}

        mm.confirmSuccess();

        return MissionConverter.toMissionSuccessConfirmDTO(memberMissionId);
    }

    // 가게 미션 생성
    @Transactional
    public Void createMission(
            Long storeId,
            MissionReqDTO.CreateMission dto
    ) {
        // 가게 찾기
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        // 미션 생성
        Mission mission = MissionConverter.toMission(store, dto);

        // 미션 DB 저장
        missionRepository.save(mission);
        return null;
    }

    // 가게 내 미션들 조회
    public MissionResDTO.Pagination<MissionResDTO.GetMission> getMissions(
            Long storeId,
            Integer pageSize,
            String cursor,
            String query
    ){
        // 페이지 정보들을 PageRequest로 만들기
        PageRequest pageRequest = PageRequest.of(0, pageSize);

        long idCursor;
        Slice<Mission> missionList;
        String nextCursor;

        // 커서가 있는 경우
        if (!cursor.equals("-1")){

            // 커서 분리
            String[] cursorSplit = cursor.split(":");
            switch (query.toLowerCase()) {
                case "id" -> {

                    // 커서 타입 변환
                    Long prevCursor = Long.parseLong(cursorSplit[0]);
                    idCursor = Long.parseLong(cursorSplit[1]);

                    // 가게 내 미션들 조회 & where절에 커서값 기입
                    missionList = missionRepository.findMissionsByStore_IdAndIdLessThanOrderByIdDesc(
                            storeId,
                            idCursor,
                            pageRequest
                    );
                }
                default -> throw new MissionException(MissionErrorCode.QUERY_NOT_VALID);
            }
        } else {
            // 커서 없이 조회
            missionList = missionRepository.findMissionsByStore_IdOrderByIdDesc(storeId, pageRequest);
        }

        // 다음 커서 계산
        nextCursor = missionList.getContent().getLast().getId()+ ":" + missionList.getContent().getLast().getId();

        // 미션들 응답 DTO로 포장하기
        return MissionConverter.toPagination(
                missionList.map(MissionConverter::toGetMission).toList(),
                missionList.hasNext(),
                nextCursor,
                missionList.getSize()
        );
    }
}
