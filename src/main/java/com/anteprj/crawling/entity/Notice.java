package com.anteprj.crawling.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_url", nullable = false)
    private String siteUrl;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;
    @Column(name = "link")
    private String link;

    public static Notice create(
            String siteUrl,
            String title,
            LocalDate publishedDate,
            String link
    ) {
        return Notice.builder()
                .siteUrl(siteUrl)
                .title(title)
                .publishedDate(publishedDate)
                .link(link)
                .build();
    }
}
