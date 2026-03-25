package com.anteprj.crawling.service.impl;

import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.crawling.service.CrawlingService;
import com.anteprj.entity.Notice;
import com.anteprj.entity.constant.NotiType;
import com.anteprj.entity.constant.SiteName;
import com.anteprj.push.service.PushService;
import com.anteprj.util.WebDriverUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class JamsilcentralparkCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private final PushService pushService;
    private final WebDriverUtil webDriverUtil;
    private static final String SITE_URL = "https://jamsilcentralpark.com/notice1/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6Mjt9&page=1";

    @Override
    @Transactional
    public void checkNewNotices() {
        WebDriver driver = webDriverUtil.getWebDriver();
        try {
            driver.get(SITE_URL);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("#accordion .acd_row, .li_board .li_body, .board-list")));

            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);
            processNotices(doc);
        } catch (Exception e) {
            log.error("[JamsilcentralparkCrawlingService] 크롤링 실패: {}", e.getMessage());
        } finally {
            webDriverUtil.quitSafely(driver);
        }
    }

    private void processNotices(Document doc) {
        Elements notices = doc.select("#accordion .acd_row");
        if (notices.isEmpty()) {
            notices = doc.select(".li_board .li_body");
        }

        if (notices.isEmpty()) {
            log.warn("[JamsilcentralparkCrawlingService] 공고 목록을 찾을 수 없음 - 사이트 구조 변경 확인 필요");
            return;
        }

        for (Element noticeElement : notices) {
            try {
                String title = noticeElement.select(".title .tabled .table-cell").text();
                if (title.isEmpty()) {
                    title = noticeElement.select(".list_text_title span").text();
                }
                if (title.isEmpty()) {
                    title = noticeElement.select("a").text();
                }
                if (title.isEmpty() || !title.contains("추가모집")) {
                    continue;
                }

                String dateText = noticeElement.select(".date div").text();
                if (dateText.isEmpty()) {
                    dateText = noticeElement.select(".time").text();
                }

                LocalDate publishedDate;
                try {
                    publishedDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    publishedDate = LocalDate.now();
                }

                boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(SITE_URL, title, publishedDate);
                if (!exists) {
                    Notice newNotice = Notice.create(
                            SiteName.JAMSIL_CENTRAL_PARK,
                            SiteName.JAMSIL_CENTRAL_PARK.getConstituency(),
                            NotiType.NOTICE,
                            SITE_URL,
                            null,
                            title,
                            publishedDate
                    );

                    noticeRepository.save(newNotice);
                    pushService.sendPush(newNotice.getSiteName().getSiteName(), title);
                }
            } catch (Exception e) {
                log.error("[JamsilcentralparkCrawlingService] 공고 파싱 실패: {}", e.getMessage());
            }
        }
    }
}
