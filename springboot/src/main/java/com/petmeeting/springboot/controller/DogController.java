package com.petmeeting.springboot.controller;

import com.petmeeting.springboot.dto.common.MessageDto;
import com.petmeeting.springboot.dto.common.ResultDto;
import com.petmeeting.springboot.dto.dog.DogReqDto;
import com.petmeeting.springboot.dto.dog.DogResDto;
import com.petmeeting.springboot.dto.dog.DogSearchCondition;
import com.petmeeting.springboot.dto.dog.DogStatusUpdateReqDto;
import com.petmeeting.springboot.service.DogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dog")
public class DogController {

    private final String ACCESS_TOKEN = "AccessToken";
    private final DogService dogService;

    @Operation(
            summary = "유기견 등록(CREATE)",
            description = "새로운 유기견을 등록합니다."
    )
    @PostMapping
    @PreAuthorize("hasRole('ROLE_SHELTER')")
    public ResponseEntity<DogResDto> createDog(@RequestBody DogReqDto reqDto, @RequestHeader(ACCESS_TOKEN) String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dogService.createDog(reqDto, token));
    }

    @Operation(
            summary = "유기견 보호상태 변경(보호소)",
            description = "해당 유기견의 보호 상태를 변경합니다. " +
                    "만약 '보호종료', '입양완료'가 되면 해당 유기견의 입양신청서가 모두 '미채택'으로 변경됩니다."
    )
    @PutMapping("/status/{dogNo}")
    @PreAuthorize("hasRole('ROLE_SHELTER')")
    public ResponseEntity<DogResDto> updateDogStatus(@PathVariable Integer dogNo, @RequestBody DogStatusUpdateReqDto reqDto, @RequestHeader(ACCESS_TOKEN) String token) {
        return ResponseEntity.ok(dogService.updateDogStatus(dogNo, reqDto, token));
    }

    @Operation(
            summary = "유기견 상세 조회",
            description = "특정 유기견을 상세 조회합니다. 로그인 해야만 권한이 있습니다."
    )
    @GetMapping("/{dogNo}")
    public ResponseEntity<DogResDto> findDog(@PathVariable Integer dogNo, @RequestHeader(ACCESS_TOKEN) String token) {
        return ResponseEntity.ok(dogService.findDog(dogNo, token));
    }

    @Operation(
            summary = "유기견 정보를 수정합니다.",
            description = "shelter의 번호와 유기견의 보호소가 일치하는 경우에만 수정됩니다."
    )
    @PutMapping("/{dogNo}")
    @PreAuthorize("hasRole('ROLE_SHELTER')")
    public ResponseEntity<DogResDto> updateDog(@PathVariable Integer dogNo, @RequestBody DogReqDto registerDogReqDto, @RequestHeader(ACCESS_TOKEN) String token) {
        return ResponseEntity.ok(dogService.updateDog(dogNo, registerDogReqDto, token));
    }

    @Operation(
            summary = "유기견 삭제",
            description = "해당 넘버의 유기견을 삭제합니다. 성공시 Delete Succuess 메세지를 반환합니다."
    )
    @DeleteMapping("/{dogNo}")
    @PreAuthorize("hasRole('ROLE_SHELTER')")
    public ResponseEntity<MessageDto> deleteDog(@PathVariable Integer dogNo, @RequestHeader(ACCESS_TOKEN) String token) {
        dogService.deleteDog(dogNo, token);
        return ResponseEntity.ok(MessageDto.msg("Delete Success"));
    }

    @Operation(
            summary = "조건에 따라 유기견 목록 조회",
            description = " 검색 조건에 따라 유기견의 목록을 반환합니다."
    )
    @GetMapping
    public ResponseEntity<List<DogResDto>> findAllDogByOption
            (@Parameter(description = "option : 'all' / 'random' / 'like' / 'rank'")
             DogSearchCondition condition, @RequestHeader(value = ACCESS_TOKEN, required = false) String token) {

        // Option : true
        if(condition.getOption() != null) {
            // 1. Option : all
            if(condition.getOption().toLowerCase().contains("all"))
                return ResponseEntity.ok(dogService.getAllDog());

            // 2. Option : Like(로그인한 유저가 좋아요한 목록)
            if(condition.getOption().toLowerCase().contains("like")) {
                if(token == null)  {
                    log.info("[로그인한 유저가 좋아요한 목록 조회] - 로그아웃 상태에선 랜덤으로 조회됩니다.");
                    return ResponseEntity.ok(dogService.getAllDogByRandom());
                }
                return ResponseEntity.ok(dogService.getLikeDogList(token));
            }

            // 3. Option : random(랜덤 정렬)
            if(condition.getOption().toLowerCase().contains("random"))
                return ResponseEntity.ok(dogService.getAllDogByRandom());

            // 4. Option : Rank(좋아요 상위) - 같은 순위일 땐 랜덤
            if(condition.getOption().toLowerCase().contains("rank"))
                return ResponseEntity.ok(dogService.getAllDogOrderByRank());
        }

        // Option : false
        return ResponseEntity.ok(dogService.findDogByCondition(condition));
    }

    @Operation(
            summary = "유기견 좋아요",
            description = "유기견 좋아요를 설정합니다."
    )
    @PostMapping("/like/{dogNo}")
    public ResponseEntity<MessageDto> likeDog(@PathVariable Integer dogNo, @RequestHeader(ACCESS_TOKEN) String token) {
        dogService.likeDog(dogNo, token);
        return ResponseEntity.ok(MessageDto.msg("Like Success"));
    }

    @Operation(
            summary = "유기견 좋아요 취소",
            description = "유기견 좋아요를 취소합니다."
    )
    @DeleteMapping("like/{dogNo}")
    public ResponseEntity<MessageDto> dislikeDog(@PathVariable Integer dogNo, @RequestHeader(ACCESS_TOKEN) String token) {
        dogService.dislikeDog(dogNo, token);
        return ResponseEntity.ok(MessageDto.msg("Dislike Success"));
    }

    @Operation(
            summary = "유기견 좋아요 상태확인(체크)",
            description = "유기견 좋아요가 눌려있는지 체크합니다."
    )
    @GetMapping("/like/{dogNo}")
    public ResponseEntity<ResultDto> checkLiked(@PathVariable Integer dogNo, @RequestHeader(ACCESS_TOKEN) String token){
        return ResponseEntity.ok(ResultDto.result(dogService.checkLiked(dogNo, token)));
    }

    @Operation(
            summary = "유기견 찜 목록 조회",
            description = "로그인한 유저의 찜 목록을 조회합니다."
    )
    @GetMapping("/bookmark")
    public ResponseEntity<List<DogResDto>> getBookmarkDogList(@RequestHeader(ACCESS_TOKEN) String token) {
        return ResponseEntity.ok(dogService.getBookmarkDogList(token));
    }

    @Operation(
            summary = "로그인한 유저의 좋아요 목록 조회",
            description = "로그인한 유저가 좋아요한 유기견 목록을 조회합니다."
    )
    @GetMapping("/likes")
    public ResponseEntity<List<DogResDto>> getLikeDogList(@RequestHeader(ACCESS_TOKEN) String token) {
        return ResponseEntity.ok(dogService.getLikeDogList(token)); // 메서드 이름 맞춤
    }

    @Operation(
            summary = "유기견 찜",
            description = "해당 유기견에게 찜을 누릅니다."
    )
    @PostMapping("/bookmark/{dogNo}")
    public ResponseEntity<MessageDto> bookmarkDog(@PathVariable Integer dogNo, @RequestHeader(ACCESS_TOKEN) String token) {
        dogService.bookmarkDog(dogNo, token);
        return ResponseEntity.ok(MessageDto.builder().msg("Bookmark Success").build());
    }

    @Operation(
            summary = "유기견 찜 취소",
            description = "해당 유기견의 찜을 취소합니다."
    )
    @DeleteMapping("/bookmark/{dogNo}")
    public ResponseEntity<MessageDto> unbookmarkDog(@PathVariable Integer dogNo, @RequestHeader(ACCESS_TOKEN) String token) {
        dogService.unbookmarkDog(dogNo, token);
        return ResponseEntity.ok(MessageDto.builder().msg("Unbookmark Success").build());
    }

    @Operation(
            summary = "유기견 찜 상태확인(체크)",
            description = "해당 유기견의 찜 유무를 체크합니다."
    )
    @GetMapping("/bookmark/{dogNo}")
    public ResponseEntity<Boolean> checkBookmarkDog(@PathVariable Integer dogNo, @RequestHeader(ACCESS_TOKEN) String token) {
        return ResponseEntity.ok(dogService.checkBookmark(dogNo, token));
    }

}
