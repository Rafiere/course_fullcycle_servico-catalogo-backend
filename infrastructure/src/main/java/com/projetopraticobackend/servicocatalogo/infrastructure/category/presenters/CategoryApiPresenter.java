package com.projetopraticobackend.servicocatalogo.infrastructure.category.presenters;

import com.projetopraticobackend.servicocatalogo.application.category.retrieve.get.CategoryOutput;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.models.CategoryApiOutput;

import java.util.function.Function;

/* Essa será uma classe útil. Uma forma de definirmos classes úteis é criarmos uma
* classe com o modificador "final", com o construtor privado e com todos os métodos
* estáticos.
*
* Uma outra maneira é definirmos uma interface e criarmos métodos estáticos
*
* Uma outra forma seria definirmos uma propriedade que é uma function. Esse modo está
* feito, mas está comentado abaixo. */
public interface CategoryApiPresenter {

    Function<CategoryOutput, CategoryApiOutput> present =
        output -> new CategoryApiOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );

    static CategoryApiOutput present(final CategoryOutput categoryOutput){
        return new CategoryApiOutput(
                categoryOutput.id().getValue(),
                categoryOutput.name(),
                categoryOutput.description(),
                categoryOutput.isActive(),
                categoryOutput.createdAt(),
                categoryOutput.updatedAt(),
                categoryOutput.deletedAt()
        );
    }
}
