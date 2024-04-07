package com.projetopraticobackend.servicocatalogo.application.category.retrieve.get;

import com.projetopraticobackend.servicocatalogo.application.category.retrieve.get.CategoryOutput;
import com.projetopraticobackend.servicocatalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;
import com.projetopraticobackend.servicocatalogo.domain.exceptions.DomainException;
import com.projetopraticobackend.servicocatalogo.domain.exceptions.NotFoundException;
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
                .orElseThrow(() -> NotFoundException.with(Category.class, CategoryID.from(id)));
    }
}
