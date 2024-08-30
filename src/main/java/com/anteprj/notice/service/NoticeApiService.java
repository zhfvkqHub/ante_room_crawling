package com.anteprj.notice.service;

import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.notice.dto.NoticeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeApiService {
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public Page<NoticeResponse> getNotice(int page, int size) {
        return noticeRepository.findNotices(
                PageRequest.of(page, size));
    }
}
