package com.anteprj.push.service;

import com.anteprj.entity.Push;
import com.anteprj.push.dto.RequestTokenDto;
import com.anteprj.push.repository.PushRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PushService {

    private final PushRepository pushRepository;

    @Transactional
    public void saveToken(RequestTokenDto requestTokenDto) {
        pushRepository.findByToken(requestTokenDto.token())
                .ifPresentOrElse(
                        push -> push.update(requestTokenDto.token()),
                        () -> pushRepository.save(Push.create(requestTokenDto.token()))
                );
    }
}
