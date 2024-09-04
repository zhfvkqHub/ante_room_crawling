package com.anteprj.crawling.controller;

import com.anteprj.crawling.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final List<CrawlingService> crawlingServices;

    @Scheduled(cron = "${scheduler.cron.crawling}")
    public void crawling() {
        for (CrawlingService service : crawlingServices) {
            service.checkNewNotices();
        }
    }
}
