package com.anteprj.crawling.repository;

import com.anteprj.notice.dto.NoticeRequest;
import com.anteprj.notice.dto.NoticeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepositoryCustom {
    Page<NoticeResponse> findNotices(NoticeRequest request, Pageable pageable);
}
