package com.example.umc10th.domain.review.controller;

import com.example.umc10th.domain.review.dto.ReviewReqDTO;
import com.example.umc10th.domain.review.dto.ReviewResDTO;
import com.example.umc10th.domain.review.exception.code.ReviewSuccessCode;
import com.example.umc10th.domain.review.service.ReviewService;
import com.example.umc10th.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping("/reviews")
    public ApiResponse<ReviewResDTO.ReviewDTO> reviews(
            @RequestBody Long memberId,
            @RequestBody ReviewReqDTO.Review reviewReq) {
        return ApiResponse.onSuccess(ReviewSuccessCode.REVIEW_WRITE_SUCCESS, reviewService.postReview(memberId, reviewReq));
    }
}
