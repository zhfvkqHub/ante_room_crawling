package com.anteprj.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "last_crawling_time")
@Getter
@Setter
@NoArgsConstructor
public class LastCrawlingTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_crawling_time")
    private LocalDateTime lastCrawlingTime;

    public void crawling(LocalDateTime lastCrawlingTime) {
        this.lastCrawlingTime = lastCrawlingTime;
    }
}
