package com.anteprj.notice.dto;

import com.anteprj.entity.constant.Constituency;
import com.anteprj.entity.constant.SiteName;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;

public record NoticeResponse(
        Long id,
        Constituency constituency,
        SiteName siteName,
        String siteUrl,
        String link,
        String title,
        LocalDate publishedDate

) {
    @QueryProjection
    public NoticeResponse {
    }
}
