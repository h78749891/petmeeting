package com.petmeeting.springboot.dto.reply;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplyResDto {

    Integer replyNo;
    Integer boardNo;
    Integer userNo;
    String writer;
    String content;
    Long createTime;
    Long modifiedTime;
    Integer likeCnt;


}
