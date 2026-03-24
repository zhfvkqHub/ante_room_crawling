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
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private static final String NOTICE_SELECTOR = "ul#recruit-list.list-type-notice > li, ul.list-type-notice#recruit-list > li, #recruit-list > li";
    private static final String BASE_URL = "https://www.elyes.co.kr";

    @Override
    @Transactional
    public void checkNewNotices() {
        WebDriver driver = webDriverUtil.getWebDriver();
        try {
            driver.get(SITE_URL);

            // 페이지 렌더링 대기 (React SPA)
            Thread.sleep(3000);

            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);

            Elements notices = doc.select(NOTICE_SELECTOR);
            if (notices.isEmpty()) {
                notices = doc.select("li a[href*=/post/recruit/detail]").parents();
            }
            if (notices.isEmpty()) {
                log.warn("[ElyesCrawlingService] 공고 목록을 찾을 수 없음 - 현재 게시글 0건이거나 사이트 구조 변경");
                return;
            }

            processNotices(notices);
        } catch (Exception e) {
            log.error("[ElyesCrawlingService] 크롤링 실패", e);
        } finally {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    log.warn("[ElyesCrawlingService] driver 종료 실패, 강제 종료 시도");
                    try {
                        Runtime.getRuntime().exec("pkill -f chromedriver");
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    private void processNotices(Elements notices) {
        for (Element noticeElement : notices) {
            try {
                Element linkElement = noticeElement.selectFirst("a[href*=/post/recruit/detail]");
                if (linkElement == null) {
                    continue;
                }

                String title = "";
                Element titleElement = linkElement.selectFirst(".list-title p, .list-title, p");
                if (titleElement != null) {
                    title = titleElement.text().trim();
                }
                if (title.isEmpty()) {
                    title = linkElement.text().trim();
                }
                if (title.isEmpty()) {
                    continue;
                }

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
                    String link = linkElement.attr("href");
                    if (link != null && !link.isEmpty() && !link.startsWith("http")) {
                        link = BASE_URL + link;
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
        Element dateElement = element.selectFirst(".date");
        String text = dateElement != null ? dateElement.text() : element.text();
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
