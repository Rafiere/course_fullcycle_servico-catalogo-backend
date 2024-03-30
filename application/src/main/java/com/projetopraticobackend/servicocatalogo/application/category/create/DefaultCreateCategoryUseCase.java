package com.projetopraticobackend.servicocatalogo.application.category.create;

import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import com.projetopraticobackend.servicocatalogo.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

/* Aqui temos a implementação do caso de uso. */

//@Named //Essa é uma anotação do "javax.inject", que é uma especificação do Java EE. O Spring
//consegue identificar essa classe e fazer a injeção dela, porém, não vamos utilizar essa abordagem, e sim, vamos
//criar esses beans manualmente na classe de infraestrutura.
public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    /* Sempre devemos fazer a injeção de dependências pelo construtor para evitarmos, por
    * exemplo, o uso de dependências cíclicas. */
    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway); //Estamos evitando uma possível NPE.
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand createCategoryCommand) {

        /* Abaixo, estamos fazendo o "destructuring" apenas por questões de
        * legibilidade, mas não precisamos fazer isso quando formos criar um código em
        * produção. */

        final var name = createCategoryCommand.name();
        final var description = createCategoryCommand.description();
        final var isActive = createCategoryCommand.isActive();

        final var notification = Notification.create(); //Estamos criando um "Notification", que é o objeto que acumulará os erros, vazio.

        //Estamos criando a categoria.
        final var category = Category.newCategory(name, description, isActive);

        /* Vamos utilizar a implementação do "NotificationHandler", ou seja, o primeiro
        * erro de validação que acontecer será acumulado ao invés de ser lançada uma exception. */
        category.validate(notification);

        return notification.hasError() ? API.Left(notification) : create(category);
    }

    private Either<Notification, CreateCategoryOutput> create(Category category) {

        //Abaixo, temos um "try" utilizando programação funcional.

        /* O Either é um mônada que representa algo com sucesso e algo com erro. É diferente do
         * "Optional", que representa algo com valor e algo sem valor. */
        return API.Try(() -> this.categoryGateway.create(category))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from); //Esse método aplica dois "maps" de uma única vez. O primeiro argumento é o "mapLeft()", ou seja, se ocorrer algum erro, e o segundo é o "mapRight()", caso a operação seja concluída com sucesso.
    }
}
