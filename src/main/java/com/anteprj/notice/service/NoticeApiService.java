package com.anteprj.notice.service;

import com.anteprj.crawling.repository.NoticeRepository;
import com.anteprj.notice.dto.NoticeResponse;
import com.anteprj.notice.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeApiService {
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;

    @Transactional(readOnly = true)
    public List<NoticeResponse> getNotice() {
        return noticeRepository.findAll().stream()
                .map(noticeMapper::toResponseDto)
                .toList();
    }
}
