package com.example.umc10th.global.util;

import com.example.umc10th.domain.review.dto.ReviewCursor;
import com.example.umc10th.domain.review.exception.code.ReviewErrorCode;
import com.example.umc10th.global.apiPayload.exception.ProjectException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CursorCodec {

    private static final ObjectMapper om = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private CursorCodec() {}

    public static String encode(ReviewCursor cursor) {
        try {
            String json = om.writeValueAsString(cursor);
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new ProjectException(ReviewErrorCode.CURSOR_INVALID);
        }
    }

    public static ReviewCursor decode(String token) {
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(token);
            String json = new String(decoded, StandardCharsets.UTF_8);
            return om.readValue(json, ReviewCursor.class);
        } catch (Exception e) {
            throw new ProjectException(ReviewErrorCode.CURSOR_INVALID);
        }
    }
}