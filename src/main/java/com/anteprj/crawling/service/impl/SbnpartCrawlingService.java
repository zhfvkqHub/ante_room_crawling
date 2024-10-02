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
        if (doc != null) {
            Elements notices = doc.select(".board-list-m ul li");
            for (Element noticeElement : notices) {
                String title = noticeElement.select(".tit a").text();

                String dateText = noticeElement.select(".info .date").text();
                LocalDate publishedDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(SITE_URL, title, publishedDate);
                if (!exists) {
                    String link = noticeElement.select(".tit a").attr("href");
                    Notice newNotice = Notice.create(
                            SiteName.SANGBONG_YANG,
                            SiteName.SANGBONG_YANG.getConstituency(),
                            getNotiType(title),
                            SITE_URL,
                            "https://sbnpart.co.kr/"+link,
                            title,
                            publishedDate
                    );

                    noticeRepository.save(newNotice);
                    pushService.sendPush(newNotice.getSiteName().getSiteName(), title);
                }
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
