package com.anteprj.notice.dto;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;

public record NoticeResponse(
        Long id,
        String siteUrl,
        String title,
        LocalDate publishedDate,
        String link

) {
    @QueryProjection
    public NoticeResponse {
    }
}
