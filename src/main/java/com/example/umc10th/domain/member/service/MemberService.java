package com.example.umc10th.domain.member.service;

import com.example.umc10th.domain.member.converter.MemberConverter;
import com.example.umc10th.domain.member.dto.MemberReqDTO;
import com.example.umc10th.domain.member.dto.MemberResDTO;
import com.example.umc10th.domain.member.entity.Member;
import com.example.umc10th.domain.member.enums.Provider;
import com.example.umc10th.domain.member.exception.MemberException;
import com.example.umc10th.domain.member.exception.code.MemberErrorCode;
import com.example.umc10th.domain.member.repository.MemberRepository;
import com.example.umc10th.domain.mission.converter.MissionConverter;
import com.example.umc10th.domain.mission.dto.MissionResDTO;
import com.example.umc10th.domain.mission.entity.Mission;
import com.example.umc10th.domain.mission.enums.Status;
import com.example.umc10th.domain.mission.repository.MemberMissionRepository;
import com.example.umc10th.domain.mission.repository.MissionRepository;
import com.example.umc10th.domain.store.entity.Region;
import com.example.umc10th.domain.store.exception.StoreException;
import com.example.umc10th.domain.store.exception.code.StoreErrorCode;
import com.example.umc10th.domain.store.repository.RegionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final MissionRepository missionRepository;
    private final MemberMissionRepository memberMissionRepository;
    private final PasswordEncoder passwordEncoder;

    // 홈 화면
    public MemberResDTO.HomeDTO getHome(Long memberId, Long regionId, LocalDate cursorEndDate, Long cursorMissionId, Integer size) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.REGION_NOT_FOUND));

        // 미션 조회 및 다음 커서 응답
        int requestSize = size;
        List<Mission> entities = missionRepository.findMissions(
                memberId, regionId, cursorEndDate, cursorMissionId,
                PageRequest.of(0, requestSize + 1)
        );

        boolean hasNext = entities.size() > requestSize;
        List<Mission> page = hasNext ? entities.subList(0, requestSize) : entities;

        List<MissionResDTO.MissionDTO> missions = page.stream()
                .map(MissionConverter::toMissionDTO)
                .toList();

        MemberResDTO.NextCursor nextCursor = null;
        if (hasNext && !page.isEmpty()) {
            Mission last = page.get(page.size() - 1);
            nextCursor = MemberResDTO.NextCursor.builder()
                    .endDate(last.getEndDate())
                    .missionId(last.getId())
                    .build();
        }

        // 지역별 완료한 미션 수 조회
        Integer completedMissionCount = memberMissionRepository.countByMemberAndRegionAndStatus(memberId, regionId, Status.SUCCESS);

        return MemberConverter.toHomeDTO(
                region.getName(),
                member.getPoint(),
                true,   // 알림 설정 여부 (나중에 변경)
                completedMissionCount,
                missions,
                hasNext,
                nextCursor
        );
    }

    // 회원가입
    public MemberResDTO.SignUpDTO signUp(MemberReqDTO.SignUp signUp) {

        Member member= Member.builder()
                .password(passwordEncoder.encode(signUp.password()))
                .name(signUp.name())
                .gender(signUp.gender())
                .birth(signUp.birth())
                .address(signUp.address())
                .email(signUp.email())
                .phone(signUp.phone())
                .socialProvider(Provider.LOCAL)
                .socialId("LOCAL")
                .point(0)
                .build();

        memberRepository.save(member);

        return MemberConverter.toSignUpDTO(member.getId());
    }

    // 마이페이지
    public MemberResDTO.MyPageDTO getMyPage(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        return MemberConverter.toMyPageDTO(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getPoint()
        );
    }

}
