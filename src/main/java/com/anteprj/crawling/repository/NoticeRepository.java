package com.anteprj.crawling.repository;

import com.anteprj.crawling.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findTopBySiteUrlOrderByPublishedDateDesc(String siteUrl);
}
