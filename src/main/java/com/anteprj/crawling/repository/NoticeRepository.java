package com.anteprj.crawling.repository;

import com.anteprj.crawling.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    boolean existsBySiteUrlAndTitleAndPublishedDate(String siteUrl, String title, LocalDate publishedDateTime);
}
