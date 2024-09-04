package com.anteprj.notice.controller;

import com.anteprj.crawling.repository.LastCrawlingTimeRepository;
import com.anteprj.entity.LastCrawlingTime;
import com.anteprj.notice.dto.NoticeRequest;
import com.anteprj.notice.dto.NoticeResponse;
import com.anteprj.notice.service.NoticeApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeApiController {

    private final NoticeApiService noticeService;
    private final LastCrawlingTimeRepository lastCrawlingTimeRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<NoticeResponse> getNotice(
            @Valid @ModelAttribute NoticeRequest request
    ) {
        return noticeService.getNotice(request);
    }

    @GetMapping("/last-crawled-time")
    public ResponseEntity<LastCrawlingTime> getLastCrawlingTime() {
        Optional<LastCrawlingTime> lastCrawlingTime = lastCrawlingTimeRepository.findTopByOrderByLastCrawlingTimeDesc();
        return ResponseEntity.ok(lastCrawlingTime.orElse(null));
    }
}
