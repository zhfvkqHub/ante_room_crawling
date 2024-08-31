package com.anteprj.crawling;

public record SiteInfo(
        String name,
        String url
) {
    public static SiteInfo of(String name, String url) {
        return new SiteInfo(name, url);
    }
}