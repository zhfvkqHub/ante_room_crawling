package com.anteprj.notice.mapper;

import com.anteprj.entity.Notice;
import com.anteprj.notice.dto.NoticeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface NoticeMapper {
        NoticeResponse toResponseDto(Notice notice);
}
