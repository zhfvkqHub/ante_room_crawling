package com.anteprj.crawling.service.impl;

import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.crawling.service.CrawlingService;
import com.anteprj.entity.Notice;
import com.anteprj.entity.constant.NotiType;
import com.anteprj.entity.constant.SiteName;
import com.anteprj.push.service.PushService;
import com.anteprj.util.JsoupUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForenaTangsanCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private final PushService pushService;
    private static final String SITE_URL = "https://www.xn--910b48b70glxklhy.com/notice.html";

    @Override
    @Transactional
    public void checkNewNotices() {
        Document doc = JsoupUtils.getDocument(SITE_URL);
        if (doc == null) {
            log.warn("[ForenaTangsanCrawlingService] 페이지 로드 실패");
            return;
        }

        Element iframe = doc.selectFirst("iframe#guest_ifr");
        if (iframe == null) {
            log.warn("[ForenaTangsanCrawlingService] iframe 요소를 찾을 수 없음");
            return;
        }

        String iframeSrc = iframe.attr("src");
        String fullIframeUrl = "https://www.xn--910b48b70glxklhy.com" + iframeSrc.replace("../", "/");

        Document iframeDoc = JsoupUtils.getDocument(fullIframeUrl);
        if (iframeDoc == null) {
            log.warn("[ForenaTangsanCrawlingService] iframe 페이지 로드 실패");
            return;
        }

        Elements notices = iframeDoc.select(".tbl_head01 tbody tr");
        int limit = Math.min(2, notices.size());
        for (int i = 0; i < limit; i++) {
            try {
                Element noticeElement = notices.get(i);
                String title = noticeElement.select("td a").text();
                if (!title.contains("모집")) {
                    continue;
                }

                String dateText = noticeElement.select("td").get(4).text().trim();
                LocalDate publishedDate = parseDate(dateText);

                boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(SITE_URL, title, publishedDate);
                if (!exists) {
                    String link = noticeElement.select("td a").attr("href");
                    Notice newNotice = Notice.create(
                            SiteName.FORENA_DANGSAN,
                            SiteName.FORENA_DANGSAN.getConstituency(),
                            NotiType.NOTICE,
                            SITE_URL,
                            link,
                            title,
                            publishedDate
                    );

                    noticeRepository.save(newNotice);
                    pushService.sendPush(newNotice.getSiteName().getSiteName(), title);
                }
            } catch (Exception e) {
                log.error("[ForenaTangsanCrawlingService] 공고 파싱 실패: {}", e.getMessage());
            }
        }
    }

    private LocalDate parseDate(String dateText) {
        if (dateText == null || dateText.isBlank()) return LocalDate.now();

        String datePart = dateText.split(" ")[0].trim();

        // "25-07-30" 형식 (yy-MM-dd)
        try {
            return LocalDate.parse(datePart, DateTimeFormatter.ofPattern("yy-MM-dd"));
        } catch (Exception ignored) {}

        // "07-30" 형식 (MM-dd) — 연도 없는 경우 현재 연도 붙이기
        try {
            return LocalDate.parse(
                    LocalDate.now().getYear() + "-" + datePart,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            log.warn("[ForenaTangsanCrawlingService] 날짜 파싱 실패: {}", dateText);
            return LocalDate.now();
        }
    }
}
