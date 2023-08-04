package com.petmeeting.springboot.enums;

import lombok.Getter;

@Getter

public enum AdoptionAvailability {

    ADOPT_POSSIBLE("입양가능"),
    ADOPT_SUCCESS("입양완료"),
    ADOPT_IMPOSSIBLE("보호종료");


    private String value;

    AdoptionAvailability(String value){
        this.value = value;
    }

    public static AdoptionAvailability getAvailability(String value) {
        if (value.equals("입양가능"))
            return ADOPT_POSSIBLE;
        else if (value.equals("입양완료"))
            return ADOPT_SUCCESS;
        else
            return ADOPT_IMPOSSIBLE;
    }
}
