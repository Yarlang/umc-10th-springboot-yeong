package com.example.umc10th.domain.mission.controller;

import com.example.umc10th.domain.mission.dto.MissionResDTO;
import com.example.umc10th.domain.mission.exception.code.MissionSuccessCode;
import com.example.umc10th.domain.mission.service.MissionService;
import com.example.umc10th.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MissionController {
    private final MissionService missionService;

    // 미션 조회
    @GetMapping("/members/me/missions")
    public ApiResponse<MissionResDTO.MemberMissionListDTO> missions(
            @RequestParam Long memberId,
            @RequestParam String status,
            @RequestParam(required = false) Long cursorMissionId,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ApiResponse.onSuccess(MissionSuccessCode. MEMBER_MISSION_LIST_SUCCESS, missionService.getMissionList(memberId, status, cursorMissionId, size));
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

}
