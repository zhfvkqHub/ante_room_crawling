package com.anteprj.crawling.service.impl;

import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.crawling.service.CrawlingService;
import com.anteprj.entity.Notice;
import com.anteprj.entity.constant.NotiType;
import com.anteprj.entity.constant.SiteName;
import com.anteprj.util.JsoupUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElandhomeCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private static final String SITE_URL = "http://elandhome.co.kr/sub/sub04_03.php";

    @Override
    @Transactional
    public void checkNewNotices() {
        Document doc = JsoupUtils.getDocument(SITE_URL);
        if (doc != null) {
            Elements notices = doc.select("tbody tr");

            for (Element noticeElement : notices) {
                String title = noticeElement.select(".subject a").text();

                String dateText = noticeElement.select(".date").text();
                LocalDate publishedDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy.MM.dd"));

                boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(SITE_URL, title, publishedDate);
                if (!exists) {
                    String link = noticeElement.select(".subject a").attr("href");
                    if (StringUtils.hasText(link)) link = "http://elandhome.co.kr/" + link;
                    Notice newNotice = Notice.create(
                            SiteName.ILAND_SINCHON,
                            SiteName.ILAND_SINCHON.getConstituency(),
                            getNotiType(title),
                            SITE_URL,
                            link,
                            title,
                            publishedDate
                    );

                    noticeRepository.save(newNotice);
                }
            }
        } else {
            log.error("Failed to retrieve document from {}", SITE_URL);
        }
    }

    private NotiType getNotiType(String title) {
        if (title.contains("명단")) {
            return NotiType.RESULT;
        } else if (title.contains("신청")) {
            return NotiType.NOTICE;
        } else {
            return NotiType.ETC;
        }
    }
}
