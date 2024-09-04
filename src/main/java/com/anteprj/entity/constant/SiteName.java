package com.anteprj.entity.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SiteName {
    SEOCHEO_FLOWER_VILLAGE_JEWELRY("서초꽃마을주얼리"),
    J_STAR_SANGBONG("제이스타상봉"),
    BX201_SEOUL_NATIONAL_UNIVERSITY("BX201서울대"),
    DONGDAEMUN_HISTORY_CULTURE_PARK("동대문역사문화공원"),
    DORIM_BRAVO("도림브라보"),
    THE_CLASSIC_DONGJAK("더클래식동작"),
    DONGJAK_GOLDEN_NOBLESS("신대방삼거리역골든노블레스"),
    ELLICE("엘리스"),
    FORENA_DANGSAN("포레나당산"),
    JAMSIL_CENTRAL_PARK("잠실센트럴파크"),
    YOUTH_SAFE_HOUSE("청년안심주택"),
    HUIKYUNG_J_STAR_SKY_CITY("휘경제이스카이시티"),
    ;

    private final String siteName;
    @JsonValue
    public String getSiteName() {
        return siteName;
    }
}
