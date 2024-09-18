package com.colak.springtutorial.service;


import com.colak.springtutorial.model.Place;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {


    private final ChatClient aiClient;

    public List<Place> searchPlaces(String prompt) {

        SystemMessage SYSTEM_MESSAGE = new SystemMessage(
                """
                        You're an assistant who helps to find lodging in San Francisco.
                        Suggest three options. Send back a JSON object in the format below.
                        [{\"name\": \"<hotel name>\", \"description\": \"<hotel description>\", \"price\": <hotel price>}]
                        Don't add any other text to the response. Don't add the new line or any other symbols to the response. Send back the raw JSON.
                        """);

        Prompt chatPrompt = new Prompt(List.of(SYSTEM_MESSAGE, new UserMessage(prompt)));

        ChatResponse chatResponse = aiClient.call(chatPrompt);

        Generation responseResult = chatResponse.getResult();
        AssistantMessage assistantMessage = responseResult.getOutput();
        String rawJson = assistantMessage.getContent();
        Place[] places = new Gson().fromJson(rawJson, Place[].class);

        return List.of(places);
    }

    // See https://medium.com/@anas.elkacemi/building-an-ocr-with-spring-boot-and-ai-my-ongoing-journey-with-handwritten-text-recognition-f9d7327095ee
    public String readImage() {
        Resource resource = new ClassPathResource("img/expenses.jpg");

        String userMessageText = """
                Analyze the image containing handwritten text and provide,
                in JSON format, how the expenses are divided as written in the image""";
        List<Media> mediaList = List.of(new Media(MediaType.IMAGE_JPEG, resource));
        UserMessage userMessage = new UserMessage(userMessageText, mediaList);

        Prompt prompt = new Prompt(userMessage);
        ChatResponse chatResponse = aiClient.call(prompt);
        Generation responseResult = chatResponse.getResult();
        AssistantMessage assistantMessage = responseResult.getOutput();
        return assistantMessage.getContent();
    }

}
