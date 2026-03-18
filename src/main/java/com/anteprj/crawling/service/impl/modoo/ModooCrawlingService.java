package com.anteprj.crawling.service.impl.modoo;

import com.anteprj.crawling.service.CrawlingService;
import lombok.extern.slf4j.Slf4j;

/**
 * 네이버 모두(modoo) 플랫폼 크롤링 서비스 - 비활성
 *
 * 2025-06-26 네이버 modoo! 서비스 종료로 인해 비활성화
 * 대상 사이트: 서초꽃마을주얼리, 제이스타상봉, BX201서울대,
 *            동대문역사문화공원, 도림브라보, 더클래식동작,
 *            휘경제이스카이시티, 센터스퀘어등촌
 *
 * 각 사이트가 새 플랫폼으로 이전 시 재구현 필요
 */
@Slf4j
public class ModooCrawlingService implements CrawlingService {

    @Override
    public void checkNewNotices() {
        // 2025-06-26 네이버 modoo! 서비스 종료로 비활성화
        log.debug("[ModooCrawlingService] 비활성 - modoo.at 서비스 종료");
    }
}
