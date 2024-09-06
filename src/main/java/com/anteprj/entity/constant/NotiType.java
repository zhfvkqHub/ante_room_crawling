package com.anteprj.entity.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum NotiType {
    NOTICE("모집공고"),
    RECEIPT("접수현황"),
    RESULT("당첨자발표"),
    ETC("기타"),
    ;

    private final String notiType;

    @JsonValue
    public String getNotiType() {
        return notiType;
    }
}
