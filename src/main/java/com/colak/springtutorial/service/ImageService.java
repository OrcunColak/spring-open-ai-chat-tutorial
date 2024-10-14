package com.colak.springtutorial.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageModel imageModel;

    public ImageResponse generateImage(String prompt) {
        ImageOptions imageOptions = ImageOptionsBuilder.builder()
                .withN(1) // Number of images to be generated
                .withHeight(1024)
                .withWidth(1024)
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(prompt, imageOptions);
        return call(imagePrompt);
    }

    private ImageResponse call(ImagePrompt prompt) {
        return imageModel.call(prompt);
    }
}
