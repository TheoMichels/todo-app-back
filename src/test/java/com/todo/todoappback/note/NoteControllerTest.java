package com.todo.todoappback.note;

import com.jayway.jsonpath.JsonPath;
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
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsAndListsNotes() throws Exception {
        mockMvc.perform(post("/notes")
                        .contentType("application/json")
                        .content("{\"title\":\"Idée\",\"description\":\"Une description\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Idée"))
                .andExpect(jsonPath("$.description").value("Une description"))
                .andExpect(jsonPath("$.createdAt").exists());

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.title == 'Idée')]").exists());
    }

    @Test
    void rejectsBlankTitle() throws Exception {
        mockMvc.perform(post("/notes")
                        .contentType("application/json")
                        .content("{\"title\":\"\",\"description\":\"x\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void updatesANote() throws Exception {
        String created = mockMvc.perform(post("/notes")
                        .contentType("application/json")
                        .content("{\"title\":\"Ancien titre\",\"description\":\"Ancienne desc\"}"))
                .andReturn().getResponse().getContentAsString();
        String noteId = JsonPath.read(created, "$.id");

        mockMvc.perform(patch("/notes/" + noteId)
                        .contentType("application/json")
                        .content("{\"title\":\"Nouveau titre\",\"description\":\"Nouvelle desc\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Nouveau titre"))
                .andExpect(jsonPath("$.description").value("Nouvelle desc"));
    }

    @Test
    void updateFailsWhenNoteMissing() throws Exception {
        mockMvc.perform(patch("/notes/00000000-0000-0000-0000-000000000000")
                        .contentType("application/json")
                        .content("{\"title\":\"x\",\"description\":\"y\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletesANote() throws Exception {
        String created = mockMvc.perform(post("/notes")
                        .contentType("application/json")
                        .content("{\"title\":\"À supprimer\",\"description\":\"\"}"))
                .andReturn().getResponse().getContentAsString();
        String noteId = JsonPath.read(created, "$.id");

        mockMvc.perform(delete("/notes/" + noteId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/notes"))
                .andExpect(jsonPath("$[?(@.title == 'À supprimer')]").doesNotExist());
    }

    @Test
    void deleteFailsWhenNoteMissing() throws Exception {
        mockMvc.perform(delete("/notes/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }
}
