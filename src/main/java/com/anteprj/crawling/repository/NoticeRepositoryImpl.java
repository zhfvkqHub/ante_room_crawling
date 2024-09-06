package com.anteprj.crawling.repository;

import com.anteprj.entity.constant.Constituency;
import com.anteprj.entity.constant.NotiType;
import com.anteprj.entity.constant.SiteName;
import com.anteprj.notice.dto.NoticeRequest;
import com.anteprj.notice.dto.NoticeResponse;
import com.anteprj.notice.dto.QNoticeResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.anteprj.entity.QNotice.notice;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NoticeResponse> findNotices(NoticeRequest request, Pageable pageable) {
        List<NoticeResponse> adminResults = queryFactory.select(
                        new QNoticeResponse(
                                notice.id,
                                notice.constituency,
                                notice.siteName,
                                notice.siteUrl,
                                notice.title,
                                notice.publishedDate
                        ))
                .from(notice)
                .where(
                        searchFilter(request.searchType(), request.searchKeyword()),
                        siteNameEq(request.siteName()),
                        notiTypeEq(request.notiType()),
                        constituencyEq(request.constituency())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notice.publishedDate.desc())
                .fetch();

        return PageableExecutionUtils.getPage(adminResults, pageable, () -> getCount(request));
    }

    private long getCount(NoticeRequest request) {
        return queryFactory.select(notice.count())
                .from(notice)
                .where(
                        searchFilter(request.searchType(), request.searchKeyword()),
                        siteNameEq(request.siteName()),
                        notiTypeEq(request.notiType()),
                        constituencyEq(request.constituency())
                )
                .fetchOne();
    }

    private BooleanExpression searchFilter(String searchType, String searchKeyword) {
        if (!hasText(searchKeyword)) {return null;}

        return switch (searchType) {
            case "ALL" -> notice.title.contains(searchKeyword);
            case "TITLE" -> notice.title.contains(searchKeyword);
            default -> throw new IllegalArgumentException("Invalid search type: " + searchType);
        };
    }

    private BooleanExpression siteNameEq(SiteName siteName) {
        return siteName != null ? notice.siteName.eq(siteName) : null;
    }

    private BooleanExpression constituencyEq(Constituency constituency) {
        return constituency != null ? notice.constituency.eq(constituency) : null;
    }

    private BooleanExpression notiTypeEq(NotiType notiType) {
        return notiType != null ? notice.notiType.eq(notiType) : null;
    }
}
