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
public class JamsilcentralparkCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private final PushService pushService;
    private static final String SITE_URL = "https://jamsilcentralpark.com/notice1/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6Mjt9&page=1";

    @Override
    @Transactional
    public void checkNewNotices() {
        Document doc = JsoupUtils.getDocument(SITE_URL);
        if (doc != null) {
            Elements notices = doc.select("#accordion .acd_row");
            for (Element noticeElement : notices) {
                String title = noticeElement.select("div").getFirst().select(".title .tabled .table-cell").text();
                if (!title.contains("추가모집")) {
                    continue;
                }

                String dateText = noticeElement.select(".date div").text();
                LocalDate publishedDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(SITE_URL, title, publishedDate);
                if (!exists) {
                    Notice newNotice = Notice.create(
                            SiteName.JAMSIL_CENTRAL_PARK,
                            SiteName.JAMSIL_CENTRAL_PARK.getConstituency(),
                            NotiType.NOTICE,
                            SITE_URL,
                            null,
                            title,
                            publishedDate
                    );

                    noticeRepository.save(newNotice);
                    pushService.sendPush(newNotice.getSiteName().getSiteName(), title);
                }
            }
        }
    }
}
