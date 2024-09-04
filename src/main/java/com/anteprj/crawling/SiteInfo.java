package com.anteprj.crawling;

import com.anteprj.entity.constant.SiteName;

public record SiteInfo(
        SiteName name,
        String url
) {
    public static SiteInfo of(SiteName name, String url) {
        return new SiteInfo(name, url);
    }
}
