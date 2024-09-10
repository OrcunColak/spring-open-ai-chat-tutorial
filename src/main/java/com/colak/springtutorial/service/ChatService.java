package com.colak.springtutorial.service;


import java.util.List;

import com.colak.springtutorial.model.Place;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final SystemMessage SYSTEM_MESSAGE = new SystemMessage(
            """
                    You're an assistant who helps to find lodging in San Francisco.
                    Suggest three options. Send back a JSON object in the format below.
                    [{\"name\": \"<hotel name>\", \"description\": \"<hotel description>\", \"price\": <hotel price>}]
                    Don't add any other text to the response. Don't add the new line or any other symbols to the response. Send back the raw JSON.
                    """);

    private final ChatClient aiClient;

    public List<Place> searchPlaces(String prompt) {
        Prompt chatPrompt = new Prompt(List.of(SYSTEM_MESSAGE, new UserMessage(prompt)));

        ChatResponse response = aiClient.call(chatPrompt);

        Generation responseResult = response.getResult();
        AssistantMessage assistantMessage = responseResult.getOutput();
        String rawJson = assistantMessage.getContent();
        Place[] places = new Gson().fromJson(rawJson, Place[].class);

        return List.of(places);
    }

}
