package com.projetopraticobackend.servicocatalogo.application.category.create;

import com.projetopraticobackend.servicocatalogo.application.category.retrieve.get.CategoryOutput;
import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;

/* O Output retornará apenas o ID da categoria criada. */
public record CreateCategoryOutput(
        CategoryID id
) {

    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId());
    }

    public static CreateCategoryOutput from(final CategoryID id){
        return new CreateCategoryOutput(id);
    }
}
