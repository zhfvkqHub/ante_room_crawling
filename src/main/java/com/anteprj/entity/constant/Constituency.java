package com.anteprj.entity.constant;

import com.anteprj.notice.dto.TypeDto;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public enum Constituency {
    GANGNAM("강남구"),
    GANGDONG("강동구"),
    GANGBUK("강북구"),
    GANGSEO("강서구"),
    GWANAK("관악구"),
    GWANGJIN("광진구"),
    GURO("구로구"),
    GEUMCHEON("금천구"),
    NOWON("노원구"),
    DOBONG("도봉구"),
    DONGDAEMUN("동대문구"),
    DONGJAK("동작구"),
    MAPO("마포구"),
    SEODAEMUN("서대문구"),
    SEOCHO("서초구"),
    SEONGDONG("성동구"),
    SEONGBUK("성북구"),
    SONGPA("송파구"),
    YANGCHEON("양천구"),
    YEONGDEUNGPO("영등포구"),
    YONGSAN("용산구"),
    EUNPYEONG("은평구"),
    JONGNO("종로구"),
    JUNG("중구"),
    JUNGNANG("중랑구"),
    HWASEONG("화성시"),
    YONGIN("용인시"),
    KIMPO("김포시"),
    ETC("기타")

    ;

    private final String constituency;

    @JsonValue
    public String getConstituency() {
        return constituency;
    }

    public static List<TypeDto> getAllConstituencies() {
        return List.of(Constituency.values())
                .stream()
                .map(con -> new TypeDto(con.getConstituency(), con.name()))
                .toList();
    }
}
