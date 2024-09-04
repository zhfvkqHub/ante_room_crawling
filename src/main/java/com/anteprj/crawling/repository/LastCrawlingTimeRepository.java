package com.anteprj.crawling.repository;

import com.anteprj.entity.LastCrawlingTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LastCrawlingTimeRepository extends JpaRepository<LastCrawlingTime, Long> {
    Optional<LastCrawlingTime> findTopByOrderByLastCrawlingTimeDesc();
}
