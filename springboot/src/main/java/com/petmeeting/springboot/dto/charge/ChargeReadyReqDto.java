package com.petmeeting.springboot.dto.charge;

import lombok.*;

@Data
public class ChargeReadyReqDto {
    private String selectPoint;
    private String approvalUrl;
    private String cancelUrl;
    private String failUrl;
}
