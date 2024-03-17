package com.projetopraticobackend.servicocatalogo.application.category.retrieve.list;

import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;

import java.time.Instant;

public record CategoryListOutput(CategoryID id,
                                 String name,
                                 String description,
                                 boolean isActive,
                                 Instant createdAt,
                                 Instant updatedAt,
                                 Instant deletedAt) {

    public static CategoryListOutput from(final Category category) {
        return new CategoryListOutput(
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
