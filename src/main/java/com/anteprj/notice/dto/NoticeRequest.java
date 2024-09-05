package com.anteprj.notice.dto;

import com.anteprj.entity.constant.Constituency;
import com.anteprj.entity.constant.SiteName;

public record NoticeRequest(
        int page,
        int size,
        SiteName siteName,
        Constituency constituency,
        String searchType,
        String searchKeyword

) {
}
