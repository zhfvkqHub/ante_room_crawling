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
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class DaebangCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private final PushService pushService;
    private final WebDriverUtil webDriverUtil;
    private static final String SITE_URL = "https://www.db40314.kr/29";

    @Override
    @Transactional
    public void checkNewNotices() {
        WebDriver driver = webDriverUtil.getWebDriver();
        try {
            driver.get(SITE_URL);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(".li_board .li_body, .board-list, [data-widget-type='board']")));

            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);
            processNotices(doc);
        } catch (Exception e) {
            log.error("[DaebangCrawlingService] 크롤링 실패: {}", e.getMessage());
        } finally {
            webDriverUtil.quitSafely(driver);
        }
    }

    private void processNotices(Document doc) {
        Elements notices = doc.select(".li_board .li_body");
        if (notices.isEmpty()) {
            log.warn("[DaebangCrawlingService] 공고 목록을 찾을 수 없음 - 사이트 구조 변경 확인 필요");
            return;
        }

        for (Element noticeElement : notices) {
            try {
                String title = noticeElement.select(".list_text_title span").text();
                if (title.isEmpty()) continue;

                String dateText = noticeElement.select(".time").text();
                LocalDate publishedDate;
                try {
                    publishedDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    publishedDate = LocalDate.now();
                }

                boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(SITE_URL, title, publishedDate);
                if (!exists) {
                    String link = noticeElement.select("a").attr("href");
                    if (StringUtils.hasText(link) && !link.startsWith("http")) {
                        link = "https://www.db40314.kr" + link;
                    }

                    Notice newNotice = Notice.create(
                            SiteName.DONGJAK_GOLDEN_NOBLESS,
                            SiteName.DONGJAK_GOLDEN_NOBLESS.getConstituency(),
                            getNotiType(title),
                            SITE_URL,
                            link,
                            title,
                            publishedDate
                    );

                    noticeRepository.save(newNotice);
                    pushService.sendPush(newNotice.getSiteName().getSiteName(), title);
                }
            } catch (Exception e) {
                log.error("[DaebangCrawlingService] 공고 파싱 실패: {}", e.getMessage());
            }
        }
    }

    private NotiType getNotiType(String title) {
        if (title.contains("발표")) {
            return NotiType.RESULT;
        } else if (title.contains("모집")) {
            return NotiType.NOTICE;
        } else {
            return NotiType.ETC;
        }
    }
}
