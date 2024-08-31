package com.anteprj.entity;

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

    @Column(name = "site_name", nullable = false)
    private String siteName;
    @Column(name = "site_url", nullable = false)
    private String siteUrl;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

    public static Notice create(
            String siteName,
            String siteUrl,
            String title,
            LocalDate publishedDate
    ) {
        return Notice.builder()
                .siteName(siteName)
                .siteUrl(siteUrl)
                .title(title)
                .publishedDate(publishedDate)
                .build();
    }
}
