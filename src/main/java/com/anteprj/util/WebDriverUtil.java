package com.anteprj.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class WebDriverUtil {

    @Value("${webdriver.chrome.path}")
    private String chromeDriverPath;

    @PostConstruct
    public void init() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    public WebDriver getWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // 브라우저 창을 띄우지 않음
        options.addArguments("--disable-gpu");  // GPU 가속을 비활성화
        options.addArguments("--no-sandbox");  // 리눅스 환경에서 필요한 옵션
        options.addArguments("--disable-dev-shm-usage");  // 리눅스 환경에서 필요한 옵션
        options.addArguments("--remote-allow-origins=*");  // 원격 디버깅 허용
        options.addArguments("--disable-software-rasterizer"); // 소프트웨어 래스터라이저 비활성화
        options.addArguments("--window-size=1920,1080");  // 가상 화면 크기 지정

        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        return driver;
    }
}
