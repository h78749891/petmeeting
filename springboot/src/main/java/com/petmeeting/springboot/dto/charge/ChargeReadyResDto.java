package com.petmeeting.springboot.dto.charge;

import lombok.*;

@Data
@Builder
public class ChargeReadyResDto {
    private String tid;
    private String nextRedirectPcUrl;
}
