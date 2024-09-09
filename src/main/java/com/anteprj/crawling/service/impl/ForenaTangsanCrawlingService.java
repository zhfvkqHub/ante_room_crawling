package com.anteprj.crawling.service.impl;

import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.crawling.service.CrawlingService;
import com.anteprj.entity.Notice;
import com.anteprj.entity.constant.NotiType;
import com.anteprj.entity.constant.SiteName;
import com.anteprj.notice.service.NotificationService;
import com.anteprj.util.JsoupUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForenaTangsanCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private final NotificationService notificationService;
    private static final String SITE_URL = "https://www.xn--910b48b70glxklhy.com/notice.html";

    @Override
    @Transactional
    public void checkNewNotices() {
        Document doc = JsoupUtils.getDocument(SITE_URL);
        if (doc != null) {
            Element iframe = doc.selectFirst("iframe#guest_ifr");
            if (iframe != null) {
                String iframeSrc = iframe.attr("src");
                String fullIframeUrl = "https://www.xn--910b48b70glxklhy.com" + iframeSrc.replace("../", "/");

                Document iframeDoc = JsoupUtils.getDocument(fullIframeUrl);
                if (iframeDoc != null) {
                    Elements notices = iframeDoc.select(".tbl_head01 tbody tr");
                    // 년도 정보가 없는 이유로 최근 3개의 공지만 확인
                    for (int i = 0; i < 3; i++) {
                        Element noticeElement = notices.get(i);
                        String title = noticeElement.select("td a").text();
                        if (!title.contains("모집")) {
                            continue;
                        }

                        String dateText = noticeElement.select("td").get(4).text();
                        LocalDate publishedDate;
                        try {
                            MonthDay monthDay = MonthDay.parse(dateText, DateTimeFormatter.ofPattern("MM-dd"));
                            publishedDate = monthDay.atYear(LocalDate.now().getYear());
                        } catch (DateTimeParseException e) {
                            // 당일 게시글의 경우 시간으로 표시되어 있음
                            log.warn("Failed to parse date: {}", dateText);
                            publishedDate = LocalDate.now();
                        }

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
                        }
                    }
                }
            }
        }
    }
}
