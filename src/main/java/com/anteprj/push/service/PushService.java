package com.anteprj.push.service;

import com.anteprj.entity.Push;
import com.anteprj.push.dto.RequestTokenDto;
import com.anteprj.push.repository.PushRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushService {

    private final FirebaseMessaging firebaseMessaging;
    private final PushRepository pushRepository;

    @Transactional
    public void saveToken(RequestTokenDto requestTokenDto) {
        pushRepository.findByToken(requestTokenDto.token())
                .ifPresentOrElse(
                        push -> push.update(requestTokenDto.token()),
                        () -> pushRepository.save(Push.create(requestTokenDto.token()))
                );
    }

    @Transactional
    public void sendPush(String title, String body) {
        List<String> pushTokens = pushRepository.findAllToken();

        for (String token : pushTokens) {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            try {
                firebaseMessaging.send(message);
            } catch (FirebaseMessagingException e) {
                MessagingErrorCode errorCode = e.getMessagingErrorCode();
                if (errorCode == MessagingErrorCode.UNREGISTERED || errorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                    log.warn("[FCM] 유효하지 않은 토큰 삭제: {}", token);
                    pushRepository.deleteByToken(token);
                } else {
                    log.error("[FCM] 푸시 발송 실패 - 토큰: {}, 에러: {}", token, e.getMessage());
                }
            } catch (Exception e) {
                log.error("[FCM] 푸시 발송 중 예외 - 토큰: {}, 에러: {}", token, e.getMessage());
            }
        }
    }

}
