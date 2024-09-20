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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CentersquaresnuCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private static final String SITE_URL = "https://centersquaresnu.com/sub/sub04_02.php";

    @Override
    @Transactional
    public void checkNewNotices() {
        Document doc = JsoupUtils.getDocument(SITE_URL);
        if (doc != null) {
            Elements notices = doc.select(".info");

            for (Element noticeElement : notices) {
                String title = noticeElement.select(".tit a").text();
                String htmlContent = noticeElement.html();

                Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
                Matcher matcher = datePattern.matcher(htmlContent);
                String dateText = null;
                if (matcher.find()) {
                    dateText = matcher.group(); // 날짜 텍스트 추출
                }

                LocalDate publishedDate;
                if (dateText != null) {
                    try {
                        publishedDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (Exception e) {
                        log.error("Failed to parse date for notice '{}': {}", title, e.getMessage());
                        continue;
                    }
                } else {
                    log.warn("No date found for notice '{}', skipping this notice.", title);
                    continue;
                }

                boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(SITE_URL, title, publishedDate);
                if (!exists) {
                    String link = noticeElement.select(".tit a").attr("href");
                    if (StringUtils.hasText(link)) link = "https://centersquaresnu.com" + link;
                    Notice newNotice = Notice.create(
                            SiteName.SEOUL_VENTURE_TOWN,
                            SiteName.SEOUL_VENTURE_TOWN.getConstituency(),
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
        if (title.contains("발표") || title.contains("결과")) {
            return NotiType.RESULT;
        } else if (title.contains("모집") || title.contains("공고")) {
            return NotiType.NOTICE;
        } else {
            return NotiType.ETC;
        }
    }
}
