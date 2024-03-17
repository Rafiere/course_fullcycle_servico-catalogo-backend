package com.projetopraticobackend.servicocatalogo.application.category.delete;

import com.projetopraticobackend.servicocatalogo.application.category.retrieve.get.CategoryOutput;
import com.projetopraticobackend.servicocatalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;
import com.projetopraticobackend.servicocatalogo.domain.exceptions.DomainException;
import com.projetopraticobackend.servicocatalogo.domain.validation.Error;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public CategoryOutput execute(final String id) {

        final var categoryId = CategoryID.from(id);

        return this.categoryGateway.findById(categoryId)
                .map(CategoryOutput::from)
                .orElseThrow(() -> DomainException.with(new Error("Category with ID %s was not found".formatted(id))));
    }
}
