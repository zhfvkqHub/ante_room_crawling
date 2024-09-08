package com.anteprj.crawling.service.impl;

import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.crawling.service.CrawlingService;
import com.anteprj.entity.Notice;
import com.anteprj.entity.constant.Constituency;
import com.anteprj.entity.constant.NotiType;
import com.anteprj.entity.constant.SiteName;
import com.anteprj.notice.service.NotificationService;
import com.anteprj.util.WebDriverUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeoulCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private final NotificationService notificationService;
    private final WebDriverUtil webDriverUtil;
    private static final String SITE_URL = "https://soco.seoul.go.kr/youth/bbs/BMSR00015/list.do?menuNo=400008";

    @Override
    @Transactional
    public void checkNewNotices() {
        WebDriver driver = webDriverUtil.getWebDriver();

        try {
            driver.get(SITE_URL);

            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);
            processNotices(doc);
        } catch (Exception e) {
            log.error("Error occurred during crawling", e);
        } finally {
            driver.quit();
        }
    }

    private void processNotices(Document doc) {
        Elements notices = doc.select(".tableWrap .boardTable tbody tr");
        for (Element noticeElement : notices) {
            String title = noticeElement.select("td a").text();
            String dateText = noticeElement.select("td").get(3).text();
            LocalDate publishedDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String constituency = title.substring(title.indexOf("[") + 1, title.indexOf("]")).replace(" ", "");
            Constituency bySiteName = SiteName.getConstituencyBySiteName(constituency);
            bySiteName = bySiteName == null ? Constituency.ETC : bySiteName;

            boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(SITE_URL, title, publishedDate);
            if (!exists) {
                Notice newNotice = Notice.create(
                        SiteName.YOUTH_SAFE_HOUSE,
                        bySiteName,
                        NotiType.NOTICE,
                        SITE_URL,
                        title,
                        publishedDate
                );

                noticeRepository.save(newNotice);
                notificationService.sendNotification(newNotice);
            }
        }
    }
}
