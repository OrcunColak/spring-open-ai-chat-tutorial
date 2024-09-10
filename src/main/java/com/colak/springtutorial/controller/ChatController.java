package com.colak.springtutorial.controller;

import com.colak.springtutorial.model.Place;
import com.colak.springtutorial.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/search")
    public List<Place> searchPlaces(@RequestParam("prompt") String prompt) {
        return chatService.searchPlaces(prompt);
    }
}
