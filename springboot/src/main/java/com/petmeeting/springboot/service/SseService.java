package com.petmeeting.springboot.service;

import com.petmeeting.springboot.dto.broadcast.SseEmitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseService {
    private final SseEmitters sseEmitters;

    public SseEmitter connect() {
        SseEmitter sseEmitter = new SseEmitter();
        sseEmitters.add(sseEmitter);

        try {
            sseEmitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sseEmitter;
    }

    public void sendMessage(String userId, long remainTime) {
        remainTime = remainTime - (System.currentTimeMillis() / 1000L);

        log.info("[SSE 메시지 전송] userId : {}, remainTime : {}", userId, remainTime);
        sseEmitters.sendMessage(userId, remainTime);
    }
}
