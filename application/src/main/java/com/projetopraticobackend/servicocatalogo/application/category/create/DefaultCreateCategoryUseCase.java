package com.projetopraticobackend.servicocatalogo.application.category.create;

import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import com.projetopraticobackend.servicocatalogo.domain.validation.handler.Notification;

import java.util.Objects;

/* Aqui temos a implementação do caso de uso. */
public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    /* Sempre devemos fazer a injeção de dependências pelo construtor para evitarmos, por
    * exemplo, o uso de dependências cíclicas. */
    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway); //Estamos evitando uma possível NPE.
    }

    @Override
    public CreateCategoryOutput execute(final CreateCategoryCommand createCategoryCommand) {

        /* Abaixo, estamos fazendo o "destructuring" apenas por questões de
        * legibilidade, mas não precisamos fazer isso quando formos criar um código em
        * produção. */

        final var name = createCategoryCommand.name();
        final var description = createCategoryCommand.description();
        final var isActive = createCategoryCommand.isActive();

        final var notification = Notification.create(); //Estamos criando um "Notification", que é o objeto que acumulará os erros, vazio.

        final var category = Category.newCategory(name, description, isActive);

        /* Vamos utilizar a implementação do "ThrowsValidationHandler", ou seja, o primeiro
        * erro que acontecer será lançado como uma "exception", ao invés de ser acumulado. */
        category.validate(notification);

        if(notification.hasError()){
            /* O Either é um mônada que representa algo com sucesso e algo com erro. */

            //Retornaremos o erro.
        }

        final var createdCategory = this.categoryGateway.create(category);

        return CreateCategoryOutput.from(createdCategory);
    }
}
