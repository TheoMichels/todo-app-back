package com.todo.todoappback.section;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsAndListsSections() throws Exception {
        mockMvc.perform(post("/sections")
                        .contentType("application/json")
                        .content("{\"name\":\"Travail\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Travail"))
                .andExpect(jsonPath("$.createdAt").exists());

        mockMvc.perform(get("/sections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name == 'Travail')]").exists());
    }

    @Test
    void rejectsBlankName() throws Exception {
        mockMvc.perform(post("/sections")
                        .contentType("application/json")
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}
