package com.anteprj.crawling.service;

import com.anteprj.crawling.entity.Notice;
import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.notice.service.NotificationService;
import com.anteprj.utile.JsoupUtils;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlingService {

    private final NoticeRepository noticeRepository;
    private final NotificationService notificationService;

    // todo database로 관리
    private final List<String> siteUrls = List.of(
            "https://www.elyes.co.kr/info/notice.do",
            "https://www.xn--910b48b70glxklhy.com/notice.html"
    );

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void checkNewNotices() {
        for (String siteUrl : siteUrls) {
            Document doc = JsoupUtils.getDocument(siteUrl);
            if (doc != null) {
                Elements notices = doc.select("CSS_SELECTOR_FOR_NOTICE"); // 적절한 CSS 선택자 사용
                for (Element noticeElement : notices) {
                    String title = noticeElement.select("CSS_SELECTOR_FOR_TITLE").text();
                    LocalDateTime publishedDate = LocalDateTime.parse(
                            noticeElement.select("CSS_SELECTOR_FOR_DATE").text(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    );

                    Notice lastNotice = noticeRepository.findTopBySiteUrlOrderByPublishedDateDesc(siteUrl)
                            .orElse(null);

                    if (lastNotice == null || publishedDate.isAfter(lastNotice.getPublishedDate())) {
                        Notice newNotice = new Notice();
                        newNotice.setSiteUrl(siteUrl);
                        newNotice.setTitle(title);
                        newNotice.setContent(noticeElement.html());
                        newNotice.setPublishedDate(publishedDate);

                        noticeRepository.save(newNotice);
                        notificationService.sendNotification(newNotice);
                    }
                }
            }
        }
    }
}
