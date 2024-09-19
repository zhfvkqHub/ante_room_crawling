package com.anteprj.notice.service;

import com.anteprj.entity.Notice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void sendFcmPush(Notice notice) {
        // firebase web push notification
    }
}
