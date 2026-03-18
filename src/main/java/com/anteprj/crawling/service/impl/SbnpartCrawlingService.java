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
public class SbnpartCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private final PushService pushService;
    private static final String SITE_URL = "https://sbnpart.co.kr/center/notice.php";

    @Override
    @Transactional
    public void checkNewNotices() {
        Document doc = JsoupUtils.getDocument(SITE_URL);
        if (doc == null) {
            log.warn("[SbnpartCrawlingService] 페이지 로드 실패");
            return;
        }

        Elements notices = doc.select(".board-list table tbody tr");
        for (Element noticeElement : notices) {
            try {
                String title = noticeElement.select("td.subject a").attr("title");
                if (title.isEmpty()) {
                    title = noticeElement.select("td.subject a").text();
                }
                if (title.isEmpty()) continue;

                String dateText = noticeElement.select("td").get(3).text().trim();
                LocalDate publishedDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(SITE_URL, title, publishedDate);
                if (!exists) {
                    String link = noticeElement.select("td.subject a").attr("href");
                    if (link != null && !link.isEmpty() && !link.startsWith("http")) {
                        link = "https://sbnpart.co.kr/" + link;
                    }

                    Notice newNotice = Notice.create(
                            SiteName.SANGBONG_YANG,
                            SiteName.SANGBONG_YANG.getConstituency(),
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
                log.error("[SbnpartCrawlingService] 공고 파싱 실패: {}", e.getMessage());
            }
        }
    }

    private NotiType getNotiType(String title) {
        if (title.contains("발표") || title.contains("결과")) {
            return NotiType.RESULT;
        } else if (title.contains("모집") || title.contains("공고")) {
            return NotiType.NOTICE;
        } else {
            return NotiType.ETC;
        }
    }
}
