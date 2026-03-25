package com.anteprj.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class WebDriverUtil {

    @Value("${webdriver.chrome.path}")
    private String chromeDriverPath;

    @PostConstruct
    public void init() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        WebDriverManager.chromedriver().setup();
    }

    public WebDriver getWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--window-size=800,600");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-translate");
        options.addArguments("--disable-background-networking");
        options.addArguments("--disable-default-apps");
        options.addArguments("--blink-settings=imagesEnabled=false");
        options.addArguments("--js-flags=--max-old-space-size=128");

        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        return driver;
    }

    public void quitSafely(WebDriver driver) {
        if (driver == null) return;
        try {
            driver.quit();
        } catch (Exception e) {
            log.warn("[WebDriverUtil] driver.quit() 실패, 강제 종료 시도");
            forceKillChromeProcesses();
        }
    }

    private void forceKillChromeProcesses() {
        killProcess("chromedriver");
        killProcess("chrome");
    }

    private void killProcess(String processName) {
        try {
            Process process = new ProcessBuilder("pkill", "-f", processName).start();
            boolean finished = process.waitFor(5, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
            }
        } catch (Exception e) {
            log.warn("[WebDriverUtil] {} 프로세스 강제 종료 실패: {}", processName, e.getMessage());
        }
    }
}
