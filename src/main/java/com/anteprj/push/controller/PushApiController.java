package com.anteprj.push.controller;

import com.anteprj.push.dto.RequestTokenDto;
import com.anteprj.push.service.PushService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/push")
public class PushApiController {

    private final PushService pushService;

    @PostMapping("/token")
    public void saveToken(
            @Valid @RequestBody RequestTokenDto requestTokenDto
    ) {
        pushService.saveToken(requestTokenDto);
    }
}
