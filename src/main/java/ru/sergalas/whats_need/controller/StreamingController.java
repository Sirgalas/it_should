package ru.sergalas.whats_need.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.sergalas.whats_need.data.PromptData;
import ru.sergalas.whats_need.services.ChatService;

import java.awt.*;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("stream")
public class StreamingController {

    private final ChatService service;

    @PostMapping(value = "chat/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sendQuestion(@RequestBody PromptData data, @PathVariable UUID id) {
        SseEmitter emitter = service.sendQuestion(data, id);
        return  emitter;
    }
}
