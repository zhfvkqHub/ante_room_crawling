package com.anteprj.notice.service;

import com.anteprj.crawling.entity.Notice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void sendNotification(Notice notice) {
        // todo 이메일, SMS, Slack 등 알림 전송 로직 구현
        log.info("New notice: {}", notice.getTitle());
    }
}
