package com.anteprj.entity;

import com.anteprj.entity.constant.Constituency;
import com.anteprj.entity.constant.NotiType;
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
import java.time.LocalDateTime;

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
    @Column(name = "noti_type", nullable = false)
    private NotiType notiType;

    @Enumerated(EnumType.STRING)
    @Column(name = "site_name", nullable = false)
    private SiteName siteName;

    @Column(name = "site_url", nullable = false)
    private String siteUrl;

    @Column(name = "link")
    private String link;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    public static Notice create(
            SiteName siteName,
            Constituency constituency,
            NotiType notiType,
            String siteUrl,
            String link,
            String title,
            LocalDate publishedDate
    ) {
        return Notice.builder()
                .siteName(siteName)
                .constituency(constituency)
                .notiType(notiType)
                .siteUrl(siteUrl)
                .link(link)
                .title(title)
                .publishedDate(publishedDate)
                .createdDate(LocalDateTime.now())
                .build();
    }
}
