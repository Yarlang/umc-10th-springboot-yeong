package com.example.umc10th.domain.member.dto;

import com.example.umc10th.domain.member.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class MemberReqDTO {

    // 회원가입
    public record SignUp(
            @NotBlank(message = "비밀번호는 필수 입니다")
            String password,
            @NotBlank(message = "이름은 필수 입니다")
            String name,
            Gender gender,
            LocalDate birth,
            String address,
            String email,
            String phone,
            List<Long> preferFoodIds,
            @Valid
            @NotNull
            Agreements agreements

    ){}

    public record Agreements(
            @NotNull(message = "개인정보 수집 및 이용 동의는 필수입니다.")
            Boolean ageAgree,
            @NotNull(message = "서비스 이용약관 동의는 필수 입니다.")
            Boolean serviceAgree,
            @NotNull(message = "개인정보 수집 및 이용 동의는 필수입니다.")
            Boolean personalAgree,
            Boolean locationAgree,
            Boolean marketingAgree
    ){}
}
