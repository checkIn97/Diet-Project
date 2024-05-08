package com.demo.persistenceTest;

import com.demo.controller.FileManageController;
import com.demo.service.FileManageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileManageController.class)
public class FileManageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileManageService fileManageService;

    @Test
    public void uploadSummernoteImageFileTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadSummernoteImageFile")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }
}