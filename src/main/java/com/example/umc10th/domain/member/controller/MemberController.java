package com.example.umc10th.domain.member.controller;

import com.example.umc10th.domain.member.dto.MemberReqDTO;
import com.example.umc10th.domain.member.dto.MemberResDTO;
import com.example.umc10th.domain.member.exception.code.MemberSuccessCode;
import com.example.umc10th.domain.member.service.MemberService;
import com.example.umc10th.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class MemberController {

    private final MemberService memberService;

    // 홈
    @GetMapping("/api/v1/members/me/home")
    public ApiResponse<MemberResDTO.HomeDTO> home(
            @RequestParam Long memberId,
            @RequestParam Long regionId,
            @RequestParam(required = false) LocalDate cursorEndDate,
            @RequestParam(required = false) Long cursorMissionId,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ApiResponse.onSuccess(MemberSuccessCode.MEMBER_HOME_SUCCESS, memberService.getHome(memberId, regionId, cursorEndDate, cursorMissionId, size));
    }

    // 회원가입
    @PostMapping("/auth/v1/members")
    public ApiResponse<MemberResDTO.SignUpDTO> signUp(@RequestBody @Valid MemberReqDTO.SignUp signUp) {
        return ApiResponse.onSuccess(MemberSuccessCode.MEMBER_SIGNUP_SUCCESS, memberService.signUp(signUp));
    }

    // 마이페이지
    @GetMapping("/api/v1/mypage")
    public ApiResponse<MemberResDTO.MyPageDTO> myPage(
            @RequestParam Long memberId
    ) {
        return ApiResponse.onSuccess(MemberSuccessCode.MEMBER_MYPAGE_SUCCESS, memberService.getMyPage(memberId));
    }
}
