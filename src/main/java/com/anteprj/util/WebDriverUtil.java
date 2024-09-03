package com.anteprj.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

@Component
public class WebDriverUtil {
    // todo properties로 path 설정
    static {
        //System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
    }

    public WebDriver getWebDriver() {
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
}
