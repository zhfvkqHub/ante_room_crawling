package com.anteprj.crawling.service;

import com.anteprj.crawling.entity.Notice;
import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.notice.service.NotificationService;
import com.anteprj.util.JsoupUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.IDN;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForenaTangsanCrawlingService {

    private final NoticeRepository noticeRepository;
    private final NotificationService notificationService;
    private final String siteUrl = "https://www.xn--910b48b70glxklhy.com/notice.html";

    public void checkNewNotices() {
        Document doc = JsoupUtils.getDocument(siteUrl);
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
                        MonthDay monthDay = MonthDay.parse(dateText, DateTimeFormatter.ofPattern("MM-dd"));
                        LocalDate publishedDate = monthDay.atYear(LocalDate.now().getYear());

                        boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(siteUrl, title, publishedDate);
                        if (!exists) {
                            String link = noticeElement.select("td a").attr("href");
                            Notice newNotice = Notice.create(siteUrl, title, publishedDate, link);

                            noticeRepository.save(newNotice);
                            notificationService.sendNotification(newNotice);
                        }
                    }
                }
            }
        }
    }
}
