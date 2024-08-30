package com.anteprj.crawling.controller;

import com.anteprj.crawling.service.ElyesCrawlingService;
import com.anteprj.crawling.service.ForenaTangsanCrawlingService;
import com.anteprj.crawling.service.SeoulCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final ElyesCrawlingService elyesCrawlingService;
    private final SeoulCrawlingService seoulCrawlingService;
    private final ForenaTangsanCrawlingService forenaTangsanCrawlingService;

     @Scheduled(fixedDelay = 60000)
     public void crawling() {
         elyesCrawlingService.checkNewNotices();
         seoulCrawlingService.checkNewNotices();
         forenaTangsanCrawlingService.checkNewNotices();
     }
}
