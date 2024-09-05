package com.anteprj.entity;

import com.anteprj.entity.constant.Constituency;
import com.anteprj.entity.constant.SiteName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "constituency", nullable = false)
    private Constituency constituency;

    @Enumerated(EnumType.STRING)
    @Column(name = "site_name", nullable = false)
    private SiteName siteName;

    @Column(name = "site_url", nullable = false)
    private String siteUrl;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

    public static Notice create(
            SiteName siteName,
            Constituency constituency,
            String siteUrl,
            String title,
            LocalDate publishedDate
    ) {
        return Notice.builder()
                .siteName(siteName)
                .constituency(constituency)
                .siteUrl(siteUrl)
                .title(title)
                .publishedDate(publishedDate)
                .build();
    }
}
