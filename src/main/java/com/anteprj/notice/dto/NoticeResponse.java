package com.anteprj.notice.dto;

import com.anteprj.entity.constant.SiteName;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;

public record NoticeResponse(
        Long id,
        SiteName siteName,
        String siteUrl,
        String title,
        LocalDate publishedDate

) {
    @QueryProjection
    public NoticeResponse {
    }
}
