package com.anteprj.notice.controller;

import com.anteprj.notice.dto.NoticeResponse;
import com.anteprj.notice.service.NoticeApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeApiController {

    private final NoticeApiService noticeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NoticeResponse> getNotice() {
        return noticeService.getNotice();
    }
}
