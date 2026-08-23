package com.todo.todoappback.todo;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Priority {
    @JsonProperty("standard") STANDARD,
    @JsonProperty("urgent") URGENT
}
