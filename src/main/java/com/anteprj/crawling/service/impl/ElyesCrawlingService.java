package com.anteprj.crawling.service.impl;

import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.crawling.service.CrawlingService;
import com.anteprj.entity.Notice;
import com.anteprj.entity.constant.Constituency;
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
public class ElyesCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private final PushService pushService;
    private final WebDriverUtil webDriverUtil;
    private static final String SITE_URL = "https://www.elyes.co.kr/post/recruit";

    @Override
    @Transactional
    public void checkNewNotices() {
        WebDriver driver = webDriverUtil.getWebDriver();
        try {
            driver.get(SITE_URL);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".board-list, .list-wrap, table, .post-list, ul.list")));

            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);
            processNotices(doc);
        } catch (Exception e) {
            log.error("[ElyesCrawlingService] 크롤링 실패: {}", e.getMessage(), e);
        } finally {
            driver.quit();
        }
    }

    private void processNotices(Document doc) {
        // 리뉴얼된 사이트의 목록 구조 탐색
        Elements notices = doc.select("table tbody tr, .board-list li, .list-wrap li, .post-list li, ul.list > li");

        if (notices.isEmpty()) {
            log.warn("[ElyesCrawlingService] 공고 목록을 찾을 수 없음 - 사이트 구조 변경 확인 필요");
            return;
        }

        for (Element noticeElement : notices) {
            try {
                String title = noticeElement.select("td a, a .title, a .tit, .subject a").text().trim();
                if (title.isEmpty()) {
                    title = noticeElement.text().trim();
                }
                if (title.isEmpty()) continue;

                String constituency;
                try {
                    constituency = title.substring(title.indexOf("[") + 1, title.indexOf("]")).replace(" ", "");
                } catch (StringIndexOutOfBoundsException e) {
                    constituency = "ETC";
                }

                Constituency bySiteName = SiteName.getConstituencyBySiteName(constituency);
                bySiteName = bySiteName == null ? Constituency.ETC : bySiteName;

                // 날짜 파싱 - 여러 포맷 시도
                LocalDate publishedDate = parseDateFromElement(noticeElement);
                if (publishedDate == null) {
                    publishedDate = LocalDate.now();
                }

                boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(SITE_URL, title, publishedDate);
                if (!exists) {
                    String link = noticeElement.select("a").attr("href");
                    if (link != null && !link.isEmpty() && !link.startsWith("http")) {
                        link = "https://www.elyes.co.kr" + link;
                    }

                    Notice newNotice = Notice.create(
                            SiteName.ELLICE,
                            bySiteName,
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
                log.error("[ElyesCrawlingService] 공고 파싱 실패: {}", e.getMessage());
            }
        }
    }

    private LocalDate parseDateFromElement(Element element) {
        String text = element.text();
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy.MM.dd"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd")
        };

        // 날짜 패턴 매칭
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("(\\d{4}[./-]\\d{2}[./-]\\d{2})")
                .matcher(text);

        if (matcher.find()) {
            String dateStr = matcher.group(1);
            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDate.parse(dateStr, formatter);
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    private NotiType getNotiType(String title) {
        if (title.contains("현황")) {
            return NotiType.RECEIPT;
        } else if (title.contains("모집공고") || title.contains("모집 공고")
                || title.contains("계약신청 안내") || title.contains("공실세대")) {
            return NotiType.NOTICE;
        } else {
            return NotiType.ETC;
        }
    }
}
