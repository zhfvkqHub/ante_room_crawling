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
    BX201_SEOUL_NATIONAL_UNIVERSITY("BX201서울대", Constituency.GWANAK),
    DONGDAEMUN_HISTORY_CULTURE_PARK("동대문역사문화공원", Constituency.JONGNO),
    DORIM_BRAVO("도림브라보", Constituency.YANGCHEON),
    THE_CLASSIC_DONGJAK("더클래식동작", Constituency.DONGJAK),
    DONGJAK_GOLDEN_NOBLESS("신대방삼거리역골든노블레스", Constituency.DONGJAK),
    FORENA_DANGSAN("포레나당산", Constituency.YEONGDEUNGPO),
    JAMSIL_CENTRAL_PARK("잠실센트럴파크", Constituency.SONGPA),
    HUIKYUNG_J_STAR_SKY_CITY("휘경제이스카이시티", Constituency.JUNGNANG),
    SEOUL_VENTURE_TOWN("서울대벤처타운역", Constituency.GWANAK),
    MHINITIUM("태릉입구역이니티움", Constituency.NOWON),
    GUSAN("구산역구산주택", Constituency.EUNPYEONG),
    LUCES_STATION("연신내역루체스테이션", Constituency.EUNPYEONG),
    SANGBONG_YANG("상봉역상봉동양엔파트", Constituency.JUNGNANG),
    SADANG_COVE("사당역코브", Constituency.DONGJAK),
    SHINPOONG_VISTA("신풍역비스타동원", Constituency.GANGSEO),

// ================== 엘리스 ==================
    ELLICE("엘리스", Constituency.ETC),
    YONGSAN_WONHYO_ROO_MINI("용산원효루미니", Constituency.YONGSAN),
    URBANIEL_CHUNGJEONG_RO("어바니엘충정로", Constituency.EUNPYEONG),
    URBANIEL_HANGANG("어바니엘한강", Constituency.YEONGDEUNGPO),
    URBANIEL_GASAN("어바니엘가산", Constituency.GEUMCHEON),
    URBANIEL_YEOMCHANG("어바니엘염창", Constituency.GANGSEO),
    URBANIEL_CHEONHO("어바니엘천호", Constituency.GANGDONG),
    MULLAE_LOTTE_CASTLE("문래롯데캐슬", Constituency.YEONGDEUNGPO),
    DONGTAN_LOTTE_CASTLE("동탄롯데캐슬", Constituency.HWASEONG),
    SUJI_LOTTE_CASTLE("수지구청역롯데캐슬", Constituency.YONGIN),
    DOKSAN_LOTTE_CASTLE("독산역롯데캐슬", Constituency.GEUMCHEON),
    HANGANG_LOTTE_CASTLE("한강롯데캐슬", Constituency.KIMPO),

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
            if (siteName.contains(site.siteName)) {// 청년 안심 주택 사이트 때문에 포함으로 처리
                return site.constituency;
            }
        }
        return null;
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
                SANGBONG_YANG, SADANG_COVE, SHINPOONG_VISTA

        );

        return List.of(SiteName.values())
                .stream()
                .filter(site -> !excludedSites.contains(site))
                .map(site -> new TypeDto(site.getSiteName(), site.name()))
                .toList();
    }

}
