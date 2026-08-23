package com.todo.todoappback.todo;

import java.time.LocalDate;

/**
 * Documentation-only shape for springdoc: the controller actually binds the raw
 * JSON body as a Map so it can tell "field omitted" (leave alone) apart from
 * "field explicitly null" (clear it) — see {@link TodoService#applyPatch}.
 */
public record TodoUpdateRequest(
        String title,
        Priority priority,
        LocalDate dueDate,
        Boolean done
) {
}
