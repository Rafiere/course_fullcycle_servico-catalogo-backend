package com.projetopraticobackend.servicocatalogo.application.category.create;

import com.projetopraticobackend.servicocatalogo.domain.category.Category;

/* O Output retornará apenas o ID da categoria criada. */
public record CreateCategoryOutput(
        String id
) {

    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId().getValue());
    }

    public static CreateCategoryOutput from(final String id){
        return new CreateCategoryOutput(id);
    }
}
