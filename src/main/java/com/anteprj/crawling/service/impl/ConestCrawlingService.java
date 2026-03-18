package com.anteprj.crawling.service.impl;

import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.crawling.service.CrawlingService;
import com.anteprj.push.service.PushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Conest 크롤링 서비스 - 현재 비활성 상태
 * 사이트 구조 변경으로 인해 크롤링 로직 미구현
 * 추후 사이트 확인 후 재활성화 필요
 */
@Slf4j
@RequiredArgsConstructor
public class ConestCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private final PushService pushService;
    private static final String SITE_URL = "http://www.conest.co.kr/";

    @Override
    public void checkNewNotices() {
        // 비활성 상태 - 사이트 구조 변경으로 크롤링 로직 미구현
        log.debug("[ConestCrawlingService] 비활성 상태 - 스킵");
    }
}
