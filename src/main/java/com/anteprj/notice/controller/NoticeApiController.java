package com.anteprj.notice.controller;

import com.anteprj.notice.dto.NoticeResponse;
import com.anteprj.notice.service.NoticeApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeApiController {

    private final NoticeApiService noticeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<NoticeResponse> getNotice(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return noticeService.getNotice(page, size);
    }
}
