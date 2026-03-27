package com.anteprj.entity.constant;

import com.anteprj.notice.dto.TypeDto;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public enum SiteName {
    // ================== 청년안심주택 ==================
    YOUTH_SAFE_HOUSE("청년안심주택", Constituency.ETC),
    SEOCHEO_FLOWER_VILLAGE_JEWELRY("서초꽃마을주얼리", Constituency.SEOCHO),
    J_STAR_SANGBONG("제이스타상봉", Constituency.JUNGNANG),
    BX201_SEOUL_NATIONAL_UNIVERSITY("BX201서울대", Constituency.GWANAK), // legacy alias
    DONGDAEMUN_HISTORY_CULTURE_PARK("동대문역사문화공원", Constituency.JONGNO),
    DORIM_BRAVO("도림브라보", Constituency.YANGCHEON),
    THE_CLASSIC_DONGJAK("더클래식동작", Constituency.DONGJAK),
    DONGJAK_GOLDEN_NOBLESS("신대방삼거리역골든노블레스", Constituency.DONGJAK),
    FORENA_DANGSAN("포레나당산", Constituency.YEONGDEUNGPO),
    JAMSIL_CENTRAL_PARK("잠실센트럴파크", Constituency.SONGPA),
    HUIKYUNG_J_STAR_SKY_CITY("휘경제이스카이시티", Constituency.JUNGNANG),
    SEOUL_VENTURE_TOWN("서울대벤처타운역", Constituency.GWANAK),
    ILAND_SINCHON("이랜드신촌", Constituency.MAPO),
    CENTER_SQUARE_DEUNGCHON("센터스퀘어등촌", Constituency.GANGSEO),
    SANGBONG_YANG("상봉역상봉동양엔파트", Constituency.JUNGNANG),

    CHEONHO_HANGANG("천호한강청년주택", Constituency.GANGDONG),
    SAGEUM_SANGBONG("세이지움상봉", Constituency.JUNGNANG),
    SEOUL_NATIONAL_UNIVERSITY_BX201("서울대입구역BX201", Constituency.GWANAK),
    SEONGBUK_FELIX("성북펠릭스", Constituency.SEONGBUK),
    YANGJAE_DOGOK_SUMMIT_TOWER("양재역도곡더써밋타워", Constituency.SEOCHO),
    ISU_STAR_PALACE("이수역스타팰리스", Constituency.SEOCHO),
    GAJWA_STAR_TOWER("가좌역스타타워", Constituency.SEODAEMUN),
    VIVA_HILLS_KANGBYUN("강변역비바힐스강변", Constituency.GWANGJIN),
    ARCHEUM_DEUNGCHON("등촌역아르체움등촌", Constituency.GANGSEO),
    IM2030_DEUNGCHON("등촌역아임2030", Constituency.GANGSEO),
    HILDESHEIM_JANGHANPYEONG("장한평역힐데스하임", Constituency.DONGDAEMUN),
    UNIT125_JANGHANPYEONG("장한평역유니트125", Constituency.DONGDAEMUN),
    MHINITIUM("태릉입구역이니티움", Constituency.NOWON),
    GUSAN("구산역구산주택", Constituency.EUNPYEONG),
    LUCES_STATION("연신내역루체스테이션", Constituency.EUNPYEONG),
    SADANG_COVE("사당역코브", Constituency.DONGJAK),
    SHINPOONG_VISTA("신풍역비스타동원", Constituency.GANGSEO),
    GAE_BONG_SAGEUM("개봉역세이지움개봉", Constituency.GANGSEO),
    NORYANGJIN_SUMMIT_TOWER("노량진역더써밋타워", Constituency.DONGJAK),

// ================== 엘리스 ==================
    ELLICE("엘리스", Constituency.ETC),
    YONGSAN_WONHYO_ROO_MINI("용산원효루미니", Constituency.YONGSAN),
    URBANIEL_CHUNGJEONG_RO("어바니엘충정로", Constituency.SEODAEMUN),
    URBANIEL_HANGANG("어바니엘한강", Constituency.DONGJAK),
    URBANIEL_GASAN("어바니엘가산", Constituency.GEUMCHEON),
    URBANIEL_YEOMCHANG("어바니엘염창", Constituency.GANGSEO),
    URBANIEL_CHEONHO("어바니엘천호", Constituency.GANGDONG),
    MULLAE_LOTTE_CASTLE("문래롯데캐슬", Constituency.YEONGDEUNGPO),
    DONGTAN_LOTTE_CASTLE("동탄롯데캐슬", Constituency.HWASEONG),
    SUJI_LOTTE_CASTLE("수지구청역롯데캐슬", Constituency.YONGIN),
    DOKSAN_LOTTE_CASTLE("독산역롯데캐슬", Constituency.GEUMCHEON),
    HANGANG_LOTTE_CASTLE("한강롯데캐슬", Constituency.KIMPO),
    HADAN_LOTTE_CASTLE("하단롯데캐슬", Constituency.BUSAN),
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
        if (siteName == null || siteName.isBlank()) {
            return null;
        }

        if (isBx201Alias(siteName)) {
            return SEOUL_NATIONAL_UNIVERSITY_BX201.constituency;
        }

        for (SiteName site : values()) {
            if (siteName.contains(site.siteName) || siteName.equals(site.name())) {
                return site.constituency;
            }
        }
        return null;
    }

    private static boolean isBx201Alias(String siteName) {
        return siteName.contains(BX201_SEOUL_NATIONAL_UNIVERSITY.siteName)
                || siteName.contains(SEOUL_NATIONAL_UNIVERSITY_BX201.siteName);
    }

    public static List<TypeDto> getAllSites() {
        List<SiteName> excludedSites = List.of(
                // 엘리스
                YONGSAN_WONHYO_ROO_MINI,
                URBANIEL_CHUNGJEONG_RO,
                URBANIEL_HANGANG,
                URBANIEL_GASAN,
                URBANIEL_YEOMCHANG,
                URBANIEL_CHEONHO,
                MULLAE_LOTTE_CASTLE,
                DONGTAN_LOTTE_CASTLE,
                SUJI_LOTTE_CASTLE,
                DOKSAN_LOTTE_CASTLE,
                HANGANG_LOTTE_CASTLE,
                // 사이트X 청년 안심주택 (추가예정)
                MHINITIUM, GUSAN, LUCES_STATION,
                SANGBONG_YANG, SADANG_COVE, SHINPOONG_VISTA,
                // BX201 legacy alias
                BX201_SEOUL_NATIONAL_UNIVERSITY
        );

        return List.of(SiteName.values())
                .stream()
                .filter(site -> !excludedSites.contains(site))
                .map(site -> new TypeDto(site.getSiteName(), site.name()))
                .toList();
    }

}
