package org.example.myblogspringboot.integration.controller;

import org.example.myblogspringboot.controller.ImageController;
import org.example.myblogspringboot.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
public class ImageControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockitoBean
    private ImageService imageService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .alwaysDo(print())
                .build();
    }

    @Test
    void getImage_shouldReturnImage() throws Exception {
        //GIVEN
        File tempFile = File.createTempFile("test", ".png");
        try {
            Files.write(tempFile.toPath(), new byte[0]);
            //WHEN
            when(imageService.findImageByPostId(anyLong()))
                    .thenReturn(tempFile);
            //THEN
            mockMvc.perform(get("/images/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_PNG));
        } finally {
            Files.deleteIfExists(tempFile.toPath());
        }
    }
}
