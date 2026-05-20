package com.example.umc10th.domain.mission.controller;

import com.example.umc10th.domain.mission.dto.MissionReqDTO;
import com.example.umc10th.domain.mission.dto.MissionResDTO;
import com.example.umc10th.domain.mission.exception.code.MissionSuccessCode;
import com.example.umc10th.domain.mission.service.MissionService;
import com.example.umc10th.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MissionController {
    private final MissionService missionService;

    // 미션 조회
    @PostMapping("/members/me/missions")
    public ApiResponse<MissionResDTO.OffsetPage<MissionResDTO.MemberMissionDTO>> missions(
            @RequestBody @Valid MissionReqDTO.MemberMission req,
            @RequestParam String status,
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize
    ) {
        return ApiResponse.onSuccess(MissionSuccessCode. MEMBER_MISSION_LIST_SUCCESS, missionService.getMissionListOffset(req.memberId(), status, pageNumber, pageSize));
    }

    // 미션 성공 요청
    @GetMapping("/members/me/missions/{missionId}/successRequest")
    public ApiResponse<MissionResDTO.MissionSuccessRequestDTO> missionSuccessRequest(
            @PathVariable Long missionId) {
        return ApiResponse.onSuccess(MissionSuccessCode.MISSION_SUCCESS_REQUEST_SUCCESS, missionService.getMissionSuccessRequest(missionId));
    }

    // 미션 성공 승인
    @PatchMapping("/owner/member-missions/{missionId}/confirm")
    public ApiResponse<MissionResDTO.MissionSuccessConfirmDTO> missionSuccessConfirm(
            @PathVariable Long missionId)
    {
        return ApiResponse.onSuccess(MissionSuccessCode.MISSION_SUCCESS_CONFIRM_SUCCESS, missionService.getMissionSuccessConfirm(missionId));
    }

    // 가게 미션 생성
    @PostMapping("/stores/{storeId}/missions")
    public ApiResponse<Long>  createMission(
            @PathVariable Long storeId,
            @RequestBody @Valid MissionReqDTO.CreateMission dto
    ){
        return ApiResponse.onSuccess(MissionSuccessCode.STORE_MISSION_CREATE_SUCCESS, missionService.createMission(storeId, dto));
    }

    // 가게 내 미션들 조회
    @GetMapping("/stores/{storeId}/missions")
    public ApiResponse<MissionResDTO.Pagination<MissionResDTO.GetMission>> getMissions(
            @PathVariable Long storeId,
            @RequestParam Integer pageSize,
            @RequestParam String cursor,
            @RequestParam String query
    ){
        return ApiResponse.onSuccess(MissionSuccessCode.STORE_MISSION_INQUIRY_SUCCESS, missionService.getMissions(storeId, pageSize, cursor, query));
    }

}
