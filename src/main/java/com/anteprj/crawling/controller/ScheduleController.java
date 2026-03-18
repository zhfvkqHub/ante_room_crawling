package com.anteprj.crawling.controller;

import com.anteprj.crawling.repository.LastCrawlingTimeRepository;
import com.anteprj.crawling.service.CrawlingService;
import com.anteprj.entity.LastCrawlingTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final List<CrawlingService> crawlingServices;
    private final LastCrawlingTimeRepository lastCrawlingTimeRepository;

    @Scheduled(cron = "${scheduler.cron.crawling}", zone = "Asia/Seoul")
    public void crawling() {
        log.info("크롤링 스케줄 시작 - 총 {}개 서비스", crawlingServices.size());

        int successCount = 0;
        int failCount = 0;

        for (CrawlingService service : crawlingServices) {
            String serviceName = service.getClass().getSimpleName();
            try {
                service.checkNewNotices();
                successCount++;
            } catch (Exception e) {
                failCount++;
                log.error("[{}] 크롤링 실패: {}", serviceName, e.getMessage(), e);
            }
        }

        log.info("크롤링 스케줄 완료 - 성공: {}, 실패: {}", successCount, failCount);

        // 크롤링 완료 시간 업데이트
        LastCrawlingTime lastCrawlingTime = lastCrawlingTimeRepository.findTopByOrderByLastCrawlingTimeDesc()
                .orElseGet(LastCrawlingTime::new);
        lastCrawlingTime.crawling(LocalDateTime.now());
        lastCrawlingTimeRepository.save(lastCrawlingTime);
    }
}
