package com.anteprj.crawling.service;

import com.anteprj.crawling.entity.Notice;
import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.notice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeoulCrawlingService {

    private final NoticeRepository noticeRepository;
    private final NotificationService notificationService;
    private final String siteUrl = "https://soco.seoul.go.kr/youth/bbs/BMSR00015/list.do?menuNo=400008";

    public void checkNewNotices() {
        System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
        WebDriver driver = getWebDriver();

        try {
            driver.get(siteUrl);

            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);
            processNotices(doc);
        } catch (Exception e) {
            log.error("Error occurred during crawling", e);
        } finally {
            driver.quit();
        }
    }

    private static WebDriver getWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // 브라우저 창을 띄우지 않음
        options.addArguments("--disable-gpu");  // GPU 가속을 비활성화
        options.addArguments("--no-sandbox");  // 리눅스 환경에서 필요한 옵션
        options.addArguments("--disable-dev-shm-usage");  // 리눅스 환경에서 필요한 옵션
        options.addArguments("--remote-allow-origins=*");  // 원격 디버깅 허용
        options.addArguments("--disable-software-rasterizer"); // 소프트웨어 래스터라이저 비활성화
        options.addArguments("--window-size=1920,1080");  // 가상 화면 크기 지정

        return new ChromeDriver(options);
    }

    private void processNotices(Document doc) {
        Elements notices = doc.select(".tableWrap .boardTable tbody tr");
        for (Element noticeElement : notices) {
            String title = noticeElement.select("td a").text();
            String dateText = noticeElement.select("td").get(3).text();
            LocalDate publishedDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            boolean exists = noticeRepository.existsBySiteUrlAndTitleAndPublishedDate(siteUrl, title, publishedDate);
            if (!exists) {
                String link = "https://soco.seoul.go.kr/youth/bbs/BMSR00015/" + noticeElement.select("tr td a").attr("href");
                Notice newNotice = Notice.create(siteUrl, title, publishedDate, link);

                noticeRepository.save(newNotice);
                notificationService.sendNotification(newNotice);
            }
        }
    }
}
