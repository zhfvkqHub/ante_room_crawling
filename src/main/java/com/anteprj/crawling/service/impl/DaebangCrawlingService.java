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
public class DaebangCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private static final String SITE_URL = "https://www.db40314.kr/29";

    @Override
    @Transactional
    public void checkNewNotices() {
        Document doc = JsoupUtils.getDocument(SITE_URL);
        if (doc != null) {
            Elements notices = doc.select(".li_board .li_body");
            for (Element noticeElement : notices) {
                String title = noticeElement.select(".list_text_title span").text();

                String dateText = noticeElement.select(".time").text();
                LocalDate publishedDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                
                boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(SITE_URL, title, publishedDate);
                if (!exists) {
                    String link = noticeElement.select("a").attr("href");
                    if(StringUtils.hasText(link)) link = "https://www.db40314.kr" + link;
                    Notice newNotice = Notice.create(
                            SiteName.DONGJAK_GOLDEN_NOBLESS, 
                            SiteName.DONGJAK_GOLDEN_NOBLESS.getConstituency(), 
                            getNotiType(title),
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

    private NotiType getNotiType(String title) {
        if (title.contains("발표")) {
            return NotiType.RESULT;
        } else if (title.contains("모집")) {
            return NotiType.NOTICE;
        } else {
            return NotiType.ETC;
        }
    }
}
