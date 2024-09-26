package com.anteprj.crawling.controller;

import com.anteprj.crawling.repository.LastCrawlingTimeRepository;
import com.anteprj.crawling.service.CrawlingService;
import com.anteprj.entity.LastCrawlingTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final List<CrawlingService> crawlingServices;
    private final LastCrawlingTimeRepository lastCrawlingTimeRepository;

    @Scheduled(cron = "${scheduler.cron.crawling}", zone = "Asia/Seoul")
    public void crawling() {
        for (CrawlingService service : crawlingServices) {
            service.checkNewNotices();
        }

        // 크롤링 완료 시간 업데이트
        LastCrawlingTime lastCrawlingTime = lastCrawlingTimeRepository.findTopByOrderByLastCrawlingTimeDesc()
                .orElseGet(LastCrawlingTime::new);
        lastCrawlingTime.crawling(LocalDateTime.now());
        lastCrawlingTimeRepository.save(lastCrawlingTime);
    }
}
