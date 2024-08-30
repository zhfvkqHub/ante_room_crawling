package com.anteprj.crawling.repository;

import com.anteprj.notice.dto.NoticeResponse;
import com.anteprj.notice.dto.QNoticeResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.anteprj.crawling.entity.QNotice.notice;

@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NoticeResponse> findNotices(Pageable pageable) {
        List<NoticeResponse> adminResults = queryFactory.select(
                        new QNoticeResponse(
                                notice.id,
                                notice.siteUrl,
                                notice.title,
                                notice.publishedDate,
                                notice.link
                        ))
                .from(notice)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notice.publishedDate.desc())
                .fetch();

        return PageableExecutionUtils.getPage(adminResults, pageable, this::getCount);
    }

    private long getCount() {
        return queryFactory.select(notice.count())
                .from(notice)
                .fetchOne();
    }
}
