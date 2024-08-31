package com.anteprj.crawling.service.impl;

import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.crawling.service.CrawlingService;
import com.anteprj.entity.Notice;
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
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElyesCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private final NotificationService notificationService;
    private static final String SITE_URL = "https://www.elyes.co.kr/info/noticeListAjax.do";

    @Override
    @Transactional
    public void checkNewNotices() {
        Document doc = JsoupUtils.getDocument(SITE_URL);
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
                    Notice newNotice = Notice.create("엘리스", link, title, publishedDate);

                    noticeRepository.save(newNotice);
                    notificationService.sendNotification(newNotice);
                }
            }
        }
    }
}
