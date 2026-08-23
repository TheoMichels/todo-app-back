package com.todo.todoappback.trackingpoint;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrackingPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createsUpdatesAndListsTrackingPoints() throws Exception {
        String created = mockMvc.perform(post("/tracking-points")
                        .contentType("application/json")
                        .content("""
                                {
                                  "title": "Livraison dépendance équipe A",
                                  "status": "En attente de validation par équipe A",
                                  "nextStep": "Relancer équipe A vendredi",
                                  "nextDueDate": "2026-08-28"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Livraison dépendance équipe A"))
                .andReturn().getResponse().getContentAsString();
        String pointId = objectMapper.readTree(created).get("id").asText();

        mockMvc.perform(get("/tracking-points"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == '" + pointId + "')]").exists());

        mockMvc.perform(patch("/tracking-points/" + pointId)
                        .contentType("application/json")
                        .content("{\"status\":\"Validé, en attente de déploiement\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Validé, en attente de déploiement"))
                .andExpect(jsonPath("$.nextStep").value("Relancer équipe A vendredi"));

        mockMvc.perform(delete("/tracking-points/" + pointId)).andExpect(status().isNoContent());
        mockMvc.perform(get("/tracking-points"))
                .andExpect(jsonPath("$[?(@.id == '" + pointId + "')]").doesNotExist());
    }

    @Test
    void titleIsRequired() throws Exception {
        mockMvc.perform(post("/tracking-points")
                        .contentType("application/json")
                        .content("{\"status\":\"...\"}"))
                .andExpect(status().isBadRequest());
    }
}
