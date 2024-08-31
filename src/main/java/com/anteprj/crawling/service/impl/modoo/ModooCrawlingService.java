package com.anteprj.crawling.service.impl.modoo;

import com.anteprj.crawling.SiteInfo;
import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.crawling.service.CrawlingService;
import com.anteprj.entity.Notice;
import com.anteprj.notice.service.NotificationService;
import com.anteprj.util.WebDriverUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModooCrawlingService implements CrawlingService {

    private final NoticeRepository noticeRepository;
    private final NotificationService notificationService;
    private final WebDriverUtil webDriverUtil;

    private static final List<SiteInfo> SITE_URLS = List.of(
            SiteInfo.of("서초꽃마을주얼리", "https://seocho1502.modoo.at/?link=8a7g4ml6"),
            SiteInfo.of("제이스타상봉", "https://jstar2030.modoo.at/?link=26i11qts"),
            SiteInfo.of("BX201서울대", "https://bx201seoul.modoo.at/?link=3gs5oxwu"),
            SiteInfo.of("동대문역사문화공원", "https://166tower.modoo.at/?link=5j8b3kfn"),
            SiteInfo.of("도림브라보", "https://dorimbravo.modoo.at/?link=286i2ogk"),
            SiteInfo.of("더클래식동작", "https://theclassic2030.modoo.at/?link=eez99qhu")
    );

    @Override
    @Transactional
    public void checkNewNotices() {
        for (SiteInfo site : SITE_URLS) {
            WebDriver driver = webDriverUtil.getWebDriver();
            try {
                driver.get(site.url());

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".table_type1 tbody")));

                String pageSource = driver.getPageSource();
                Document doc = Jsoup.parse(pageSource);
                processNotices(doc, site.url(), site.name());
            } catch (Exception e) {
                log.error("Error occurred during crawling", e);
            } finally {
                driver.quit();
            }
        }
    }

    private void processNotices(Document doc, String siteUrl, String siteName) {
        Elements notices = doc.select(".table_type1 tbody tr");
        for (Element noticeElement : notices) {
            String title = noticeElement.select("td a").text();
            String dateText = noticeElement.select("td").get(3).text();
            LocalDate publishedDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy.M.d"));

            boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(siteUrl, title, publishedDate);
            if (!exists) {
                Notice newNotice = Notice.create(siteName, siteUrl, title, publishedDate);

                noticeRepository.save(newNotice);
                notificationService.sendNotification(newNotice);
            }
        }
    }
}

