package com.projetopraticobackend.servicocatalogo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.core.util.Json;

import java.time.Instant;

public record CategoryAPIOutput(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") boolean active,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {


}