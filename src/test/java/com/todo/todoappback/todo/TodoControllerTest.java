package com.todo.todoappback.todo;

import tools.jackson.databind.JsonNode;
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
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String createSection(String name) throws Exception {
        String body = mockMvc.perform(post("/sections")
                        .contentType("application/json")
                        .content("{\"name\":\"" + name + "\"}"))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(body).get("id").asText();
    }

    @Test
    void createTodoFailsWhenSectionMissing() throws Exception {
        String randomSectionId = "00000000-0000-0000-0000-000000000000";
        mockMvc.perform(post("/todos")
                        .contentType("application/json")
                        .content("{\"sectionId\":\"" + randomSectionId + "\",\"title\":\"Test\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createDefaultsToStandardPriorityAndNoDueDate() throws Exception {
        String sectionId = createSection("Général");

        mockMvc.perform(post("/todos")
                        .contentType("application/json")
                        .content("{\"sectionId\":\"" + sectionId + "\",\"title\":\"Envoyer le rapport\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.priority").value("standard"))
                .andExpect(jsonPath("$.done").value(false))
                .andExpect(jsonPath("$.dueDate").doesNotExist());
    }

    @Test
    void checkingATodoMovesItOutOfItsSectionAndIntoDone() throws Exception {
        String sectionId = createSection("Travail");

        String created = mockMvc.perform(post("/todos")
                        .contentType("application/json")
                        .content("{\"sectionId\":\"" + sectionId + "\",\"title\":\"Préparer la réunion\",\"priority\":\"urgent\"}"))
                .andReturn().getResponse().getContentAsString();
        JsonNode todo = objectMapper.readTree(created);
        String todoId = todo.get("id").asText();

        // Visible in the active (done=false) view for its section.
        mockMvc.perform(get("/todos").param("sectionId", sectionId).param("done", "false"))
                .andExpect(jsonPath("$[?(@.id == '" + todoId + "')]").exists());

        // Check it: PATCH done=true.
        mockMvc.perform(patch("/todos/" + todoId)
                        .contentType("application/json")
                        .content("{\"done\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(true));

        // Gone from the active view...
        mockMvc.perform(get("/todos").param("sectionId", sectionId).param("done", "false"))
                .andExpect(jsonPath("$[?(@.id == '" + todoId + "')]").doesNotExist());

        // ...and present in the global "Supprimés" view (done=true, no sectionId filter).
        mockMvc.perform(get("/todos").param("done", "true"))
                .andExpect(jsonPath("$[?(@.id == '" + todoId + "')]").exists());
    }

    @Test
    void clearingDueDateWithExplicitNullWorks() throws Exception {
        String sectionId = createSection("Général");
        String created = mockMvc.perform(post("/todos")
                        .contentType("application/json")
                        .content("{\"sectionId\":\"" + sectionId + "\",\"title\":\"Avec échéance\",\"dueDate\":\"2026-08-30\"}"))
                .andReturn().getResponse().getContentAsString();
        String todoId = objectMapper.readTree(created).get("id").asText();

        mockMvc.perform(patch("/todos/" + todoId)
                        .contentType("application/json")
                        .content("{\"dueDate\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dueDate").doesNotExist());
    }

    @Test
    void patchUpdatesTitlePriorityAndDueDate() throws Exception {
        String sectionId = createSection("Général");
        String created = mockMvc.perform(post("/todos")
                        .contentType("application/json")
                        .content("{\"sectionId\":\"" + sectionId + "\",\"title\":\"Initial\"}"))
                .andReturn().getResponse().getContentAsString();
        String todoId = objectMapper.readTree(created).get("id").asText();

        mockMvc.perform(patch("/todos/" + todoId)
                        .contentType("application/json")
                        .content("{\"title\":\"  Titre MAJ  \",\"priority\":\"URGENT\",\"dueDate\":\"2026-12-31\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Titre MAJ"))
                .andExpect(jsonPath("$.priority").value("urgent"))
                .andExpect(jsonPath("$.dueDate").value("2026-12-31"));
    }

    @Test
    void patchRejectsBlankTitle() throws Exception {
        String sectionId = createSection("Général");
        String created = mockMvc.perform(post("/todos")
                        .contentType("application/json")
                        .content("{\"sectionId\":\"" + sectionId + "\",\"title\":\"Quelque chose\"}"))
                .andReturn().getResponse().getContentAsString();
        String todoId = objectMapper.readTree(created).get("id").asText();

        mockMvc.perform(patch("/todos/" + todoId)
                        .contentType("application/json")
                        .content("{\"title\":\"   \"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchRejectsInvalidPriority() throws Exception {
        String sectionId = createSection("Général");
        String created = mockMvc.perform(post("/todos")
                        .contentType("application/json")
                        .content("{\"sectionId\":\"" + sectionId + "\",\"title\":\"Quelque chose\"}"))
                .andReturn().getResponse().getContentAsString();
        String todoId = objectMapper.readTree(created).get("id").asText();

        mockMvc.perform(patch("/todos/" + todoId)
                        .contentType("application/json")
                        .content("{\"priority\":\"CRITICAL\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchOnUnknownTodoReturns404() throws Exception {
        String randomTodoId = "00000000-0000-0000-0000-000000000000";
        mockMvc.perform(patch("/todos/" + randomTodoId)
                        .contentType("application/json")
                        .content("{\"done\":true}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRemovesTodoForGood() throws Exception {
        String sectionId = createSection("Général");
        String created = mockMvc.perform(post("/todos")
                        .contentType("application/json")
                        .content("{\"sectionId\":\"" + sectionId + "\",\"title\":\"À supprimer\"}"))
                .andReturn().getResponse().getContentAsString();
        String todoId = objectMapper.readTree(created).get("id").asText();

        mockMvc.perform(delete("/todos/" + todoId)).andExpect(status().isNoContent());
        mockMvc.perform(patch("/todos/" + todoId)
                        .contentType("application/json")
                        .content("{\"done\":true}"))
                .andExpect(status().isNotFound());
    }
}
