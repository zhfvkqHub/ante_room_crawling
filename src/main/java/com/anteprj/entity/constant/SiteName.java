package com.anteprj.entity.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SiteName {
    SEOCHEO_FLOWER_VILLAGE_JEWELRY("서초꽃마을주얼리", Constituency.SEOCHO),
    J_STAR_SANGBONG("제이스타상봉", Constituency.JUNGNANG),
    BX201_SEOUL_NATIONAL_UNIVERSITY("BX201서울대", Constituency.GWANAK),
    DONGDAEMUN_HISTORY_CULTURE_PARK("동대문역사문화공원", Constituency.JONGNO),
    DORIM_BRAVO("도림브라보", Constituency.YANGCHEON),
    THE_CLASSIC_DONGJAK("더클래식동작", Constituency.DONGJAK),
    DONGJAK_GOLDEN_NOBLESS("신대방삼거리역골든노블레스", Constituency.DONGJAK),
    FORENA_DANGSAN("포레나당산", Constituency.YEONGDEUNGPO),
    JAMSIL_CENTRAL_PARK("잠실센트럴파크", Constituency.SONGPA),
    HUIKYUNG_J_STAR_SKY_CITY("휘경제이스카이시티", Constituency.JUNGNANG),
    ELLICE("엘리스", Constituency.ETC),
    // TODO ellice 사이트
    // 용산원효루미니
    YONGSAN_WONHYO_ROO_MINI("용산원효루미니", Constituency.YONGSAN),
    // 문래 롯데캐슬
    MULLAE_LOTTE_CASTLE("문래롯데캐슬", Constituency.YEONGDEUNGPO),
    // 어바니엘 충정로
    URBANIEL_CHUNGJEONG_RO("어바니엘충정로", Constituency.EUNPYEONG),
    YOUTH_SAFE_HOUSE("청년안심주택", Constituency.ETC),
    // TODO 청년안심주택 사이트
    ;

    private final String siteName;
    private final Constituency constituency;
    @JsonValue
    public String getSiteName() {
        return siteName;
    }

    public Constituency getConstituency() {
        return constituency;
    }

    public static Constituency getConstituencyBySiteName(String siteName) {
        for (SiteName site : values()) {
            if (site.siteName.equalsIgnoreCase(siteName)) {
                return site.constituency;
            }
        }
        return null;
    }
}
