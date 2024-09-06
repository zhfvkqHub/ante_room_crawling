package com.anteprj.crawling.service.impl.modoo;

import com.anteprj.crawling.SiteInfo;
import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.crawling.service.CrawlingService;
import com.anteprj.entity.Notice;
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
            SiteInfo.of(SiteName.SEOCHEO_FLOWER_VILLAGE_JEWELRY, "https://seocho1502.modoo.at/?link=8a7g4ml6"),
            SiteInfo.of(SiteName.J_STAR_SANGBONG, "https://jstar2030.modoo.at/?link=26i11qts"),
            SiteInfo.of(SiteName.BX201_SEOUL_NATIONAL_UNIVERSITY, "https://bx201seoul.modoo.at/?link=3gs5oxwu"),
            SiteInfo.of(SiteName.DONGDAEMUN_HISTORY_CULTURE_PARK, "https://166tower.modoo.at/?link=5j8b3kfn"),
            SiteInfo.of(SiteName.DORIM_BRAVO, "https://dorimbravo.modoo.at/?link=286i2ogk"),
            SiteInfo.of(SiteName.THE_CLASSIC_DONGJAK, "https://theclassic2030.modoo.at/?link=eez99qhu"),
            SiteInfo.of(SiteName.HUIKYUNG_J_STAR_SKY_CITY, "https://hkjskycity.modoo.at/?link=f3scs7qg")
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

    private void processNotices(Document doc, String siteUrl, SiteName siteName) {
        Elements notices = doc.select(".table_type1 tbody tr");
        for (Element noticeElement : notices) {
            String title = noticeElement.select("td a").text();
            if (title.contains("계약완료")) {
                continue;
            }

            String dateText = noticeElement.select("td").get(3).text();

            LocalDate publishedDate;
            if (dateText.contains("시간")) {
                publishedDate = LocalDate.now();
            } else {
                publishedDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy.M.d"));
            }

            boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(siteUrl, title, publishedDate);
            if (!exists) {
                Notice newNotice = Notice.create(
                        siteName,
                        siteName.getConstituency(),
                        getNotiType(title),
                        siteUrl,
                        title,
                        publishedDate
                );

                noticeRepository.save(newNotice);
                notificationService.sendNotification(newNotice);
            }
        }
    }

    private NotiType getNotiType(String title) {
        if (title.contains("추첨 결과") || title.contains("발표") || title.contains("완료")) {
            return NotiType.RESULT;
        } else if (title.contains("모집") || title.contains("공실")) {
            return NotiType.NOTICE;
        } else {
            return NotiType.ETC;
        }
    }
}
