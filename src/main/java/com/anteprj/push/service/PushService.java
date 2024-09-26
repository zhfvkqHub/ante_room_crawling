package com.anteprj.push.service;

import com.anteprj.entity.Push;
import com.anteprj.push.dto.RequestTokenDto;
import com.anteprj.push.repository.PushRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                String response = firebaseMessaging.send(message);
                System.out.println("Successfully sent message: " + response);
            } catch (Exception e) {
                System.err.println("Error sending FCM message: " + e.getMessage());
            }
        }
    }

}
