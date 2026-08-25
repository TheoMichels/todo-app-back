package com.todo.todoappback.section;

import com.todo.todoappback.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    public Section create(String name) {
        return sectionRepository.save(new Section(name));
    }

    public Section update(UUID sectionId, String name) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section introuvable"));
        section.setName(name.trim());
        return sectionRepository.save(section);
    }
}
