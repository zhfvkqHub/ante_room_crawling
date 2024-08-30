package com.anteprj.notice.dto;

public record NoticeResponse(
        Long id,
        String siteUrl,
        String title,
        String publishedDate,
        String link

) {
}
