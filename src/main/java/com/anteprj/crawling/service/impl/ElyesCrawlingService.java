package com.anteprj.crawling.service.impl;

import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.crawling.service.CrawlingService;
import com.anteprj.entity.Notice;
import com.anteprj.entity.constant.Constituency;
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

                String constituency = title.substring(title.indexOf("[") + 1, title.indexOf("]")).replace(" ", "");
                Constituency bySiteName = SiteName.getConstituencyBySiteName(constituency);
                bySiteName = bySiteName == null ? Constituency.ETC : bySiteName;

                LocalDate publishedDate = LocalDate.parse(
                        noticeElement.select("td").get(2).text(),
                        DateTimeFormatter.ofPattern("yyyy.MM.dd")
                );
                String siteUrl = "https://www.elyes.co.kr/info/notice.do";

                // 해당 공지사항이 이미 존재하는지 확인
                boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(siteUrl, title, publishedDate);
                if (!exists) {
                    Notice newNotice = Notice.create(
                            SiteName.ELLICE,
                            bySiteName, 
                            getNotiType(title),
                            siteUrl,
                            null,
                            title,
                            publishedDate
                    );

                    noticeRepository.save(newNotice);
                    notificationService.sendNotification(newNotice);
                }
            }
        }
    }

    private NotiType getNotiType(String title) {
        if (title.contains("접수현황")) {
            return NotiType.RECEIPT;
        } else if (title.contains("모집공고") || title.contains("모집 공고")) {
            return NotiType.NOTICE;
        } else {
            return NotiType.ETC;
        }
    }
}
