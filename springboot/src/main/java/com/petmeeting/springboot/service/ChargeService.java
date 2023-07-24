package com.petmeeting.springboot.service;

import com.petmeeting.springboot.domain.Charge;
import com.petmeeting.springboot.domain.Member;
import com.petmeeting.springboot.domain.Users;
import com.petmeeting.springboot.dto.charge.*;
import com.petmeeting.springboot.repository.ChargeRepository;
import com.petmeeting.springboot.repository.UserRepository;
import com.petmeeting.springboot.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChargeService {
    @Value("${kakao.admin_key}")
    private String ADMIN_KEY;
    @Value("${kakao.content_type}")
    private String CONTENT_TYPE;
    @Value("${kakao.cid}")
    private String CID;

    private final UserRepository userRepository;
    private final ChargeRepository chargeRepository;
    private final String KAKAO_READY_URL = "https://kapi.kakao.com/v1/payment/ready";
    private final JwtUtils jwtUtils;

    /**
     * 결제요청
     * 결제요청오면 카카오페이와 연결
     * @param chargeReadyReqDto
     * @param token
     * @return tid, redirect url
     */
    public ChargeReadyResDto ready(ChargeReadyReqDto chargeReadyReqDto, String token) {
        int userNo = getUserNo(token);
        Users user = userRepository.findById(userNo).get();

        log.info("[결제요청] userId : {} / price : {}", user.getUserId(), chargeReadyReqDto.getSelectPoint());

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        requestParams.add("cid", CID);
        requestParams.add("partner_order_id", "PetMeeting");
        requestParams.add("partner_user_id", user.getUserId());
        requestParams.add("item_name", chargeReadyReqDto.getSelectPoint() + "포인트 + "
                + chargeReadyReqDto.getSelectToken() + "토큰 충전하기");
        requestParams.add("quantity", "1");
        requestParams.add("total_amount", chargeReadyReqDto.getSelectPoint());
        requestParams.add("tax_free_amount", "0");
        requestParams.add("approval_url", chargeReadyReqDto.getApprovalUrl());
        requestParams.add("cancel_url", chargeReadyReqDto.getCancelUrl());
        requestParams.add("fail_url", chargeReadyReqDto.getFailUrl());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestParams, this.getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        KakaoReadyResDto kakaoReadyResDto = restTemplate
                .postForObject(KAKAO_READY_URL, requestEntity, KakaoReadyResDto.class);

        return ChargeReadyResDto.builder()
                .tid(kakaoReadyResDto.getTid())
                .nextRedirectPcUrl(kakaoReadyResDto.getNext_redirect_pc_url()).build();
    }

    /**
     * 결제 검증
     * 결제완료 시 tid와 pg_token을 이용하여 결제내역 확인
     * @param chargeCheckReqDto
     * @param token
     * @return chargedPrice, chargedToken
     */
    @Transactional
    public ChargeCheckResDto check(ChargeCheckReqDto chargeCheckReqDto, String token) {
        int userNo = getUserNo(token);
        Member member = (Member) userRepository.findById(userNo).get();

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        requestParams.add("cid", CID);
        requestParams.add("tid", chargeCheckReqDto.getTid());
        requestParams.add("partner_order_id", "PetMeeting");
        requestParams.add("partner_user_id", member.getUserId());
        requestParams.add("pg_token", chargeCheckReqDto.getPgToken());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestParams, this.getHeaders());

        RestTemplate restTemplate = new RestTemplate();
        KakaoApproveResDto kakaoApproveResDto = restTemplate
                .postForObject("https://kapi.kakao.com/v1/payment/approve", requestEntity, KakaoApproveResDto.class);

        int chargePrice = kakaoApproveResDto.getAmount().getTotal();
        int chargeToken = chargePrice < 10000 ? 1 : (chargePrice < 50000 ? 2 : 3); // 토큰 개수를 직접 입력받거나 표를 정해야 함.

        Charge charge = Charge.builder()
                .member(member)
                .tid(kakaoApproveResDto.getTid())
                .chargeValue(chargePrice)
                .chargeTime((int) ((long) System.currentTimeMillis() / 1000L))
                .build();

        chargeRepository.save(charge);

        log.info("[결제검증] userId : {}, chargePrice : {}, chargeToken : {}", member.getUserId(), chargePrice, chargeToken);

        member.chargeTokens(chargeToken);

        return ChargeCheckResDto.builder()
                .price(chargePrice)
                .addToken(chargeToken)
                .holdingToken(member.getHoldingToken())
                .addPoint(chargePrice)
                .holdingPoint(chargeRepository.findSumByUserNo(member))
                .build();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + ADMIN_KEY;
        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-Type", CONTENT_TYPE);

        return httpHeaders;
    }

    private Integer getUserNo(String token) {
        if (!token.startsWith("Bearer ")) {
            log.error("[토큰 검증] Prefix Error");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prefix가 올바르지 않습니다.");
        }
        token = token.substring(7);

        if (!jwtUtils.validateJwtToken(token))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다.");

        return jwtUtils.getUserNoFromJwtToken(token);
    }
}
