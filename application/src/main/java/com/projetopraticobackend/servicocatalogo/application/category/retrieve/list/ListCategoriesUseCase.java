package com.projetopraticobackend.servicocatalogo.application.category.retrieve.list;

import com.projetopraticobackend.servicocatalogo.application.UseCase;
import com.projetopraticobackend.servicocatalogo.domain.category.CategorySearchQuery;
import com.projetopraticobackend.servicocatalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
