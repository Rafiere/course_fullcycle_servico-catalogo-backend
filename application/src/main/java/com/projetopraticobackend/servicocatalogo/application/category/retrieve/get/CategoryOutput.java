package com.projetopraticobackend.servicocatalogo.application.category.retrieve.get;

import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;

import java.time.Instant;

public record CategoryOutput(CategoryID id,
                             String name,
                             String description,
                             boolean isActive,
                             Instant createdAt,
                             Instant updatedAt,
                             Instant deletedAt) {

    public static CategoryOutput from(Category category) {
        return new CategoryOutput(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt()
        );
    }
}
