package com.todo.todoappback.section;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping
    public List<SectionResponse> list() {
        return sectionService.findAll().stream().map(SectionResponse::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SectionResponse create(@Valid @RequestBody SectionCreateRequest request) {
        return SectionResponse.from(sectionService.create(request.name()));
    }

    @PatchMapping("/{sectionId}")
    public SectionResponse update(
            @PathVariable UUID sectionId,
            @Valid @RequestBody SectionUpdateRequest request
    ) {
        return SectionResponse.from(sectionService.update(sectionId, request.name()));
    }
}
