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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElyesCrawlingService {

    private final NoticeRepository noticeRepository;
    private final NotificationService notificationService;
    private final String siteUrl = "https://www.elyes.co.kr/info/noticeListAjax.do";

    public void checkNewNotices() {
        Document doc = JsoupUtils.getDocument(siteUrl);
        if (doc != null) {
            Elements notices = doc.select(".lotte-tr-toggle tbody tr");
            for (Element noticeElement : notices) {
                String title = noticeElement.select("td a").text();
                LocalDate publishedDate = LocalDate.parse(
                        noticeElement.select("td").get(2).text(),
                        DateTimeFormatter.ofPattern("yyyy.MM.dd")
                );
                String link = "https://www.elyes.co.kr/info/notice.do";

                // 해당 공지사항이 이미 존재하는지 확인
                boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(link, title, publishedDate);
                if (!exists) {
                    Notice newNotice = Notice.create(link, title, publishedDate, link);

                    noticeRepository.save(newNotice);
                    notificationService.sendNotification(newNotice);
                }
            }
        }
    }
}
