package com.projetopraticobackend.servicocatalogo.application.category.retrieve.list;

import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import com.projetopraticobackend.servicocatalogo.domain.category.CategorySearchQuery;
import com.projetopraticobackend.servicocatalogo.domain.pagination.Pagination;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Pagination<CategoryListOutput> execute(final CategorySearchQuery categorySearchQuery) {
        return this.categoryGateway.findAll(categorySearchQuery)
                .map(CategoryListOutput::from);
    }
}
