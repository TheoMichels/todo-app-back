package com.todo.todoappback.todo;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<TodoResponse> list(
            @RequestParam(required = false) UUID sectionId,
            @RequestParam(required = false) Boolean done
    ) {
        return todoService.find(sectionId, done).stream().map(TodoResponse::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponse create(@Valid @org.springframework.web.bind.annotation.RequestBody TodoCreateRequest request) {
        return TodoResponse.from(todoService.create(request));
    }

    @PatchMapping("/{todoId}")
    @RequestBody(content = @Content(schema = @Schema(implementation = TodoUpdateRequest.class)))
    public TodoResponse update(
            @PathVariable UUID todoId,
            @org.springframework.web.bind.annotation.RequestBody Map<String, Object> patch
    ) {
        return TodoResponse.from(todoService.applyPatch(todoId, patch));
    }

    @DeleteMapping("/{todoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID todoId) {
        todoService.delete(todoId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll(@org.springframework.web.bind.annotation.RequestBody List<UUID> ids) {
        todoService.deleteAll(ids);
    }
}
