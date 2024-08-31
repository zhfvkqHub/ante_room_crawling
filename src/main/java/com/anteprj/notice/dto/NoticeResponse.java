package com.anteprj.notice.dto;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;

public record NoticeResponse(
        Long id,
        String siteName,
        String siteUrl,
        String title,
        LocalDate publishedDate

) {
    @QueryProjection
    public NoticeResponse {
    }
}
