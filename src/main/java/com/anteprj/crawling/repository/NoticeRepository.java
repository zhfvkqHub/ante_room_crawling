package com.anteprj.crawling.repository;

import com.anteprj.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {

    boolean existsBySiteUrlAndTitleAndPublishedDate(String siteUrl, String title, LocalDate publishedDateTime);
}
