package com.projetopraticobackend.servicocatalogo.application.category.update;

import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;
import com.projetopraticobackend.servicocatalogo.domain.exceptions.DomainException;
import com.projetopraticobackend.servicocatalogo.domain.validation.Error;
import com.projetopraticobackend.servicocatalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Try;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand command) {

        final var id = CategoryID.from(command.id());

        String name = command.name();
        String description = command.description();
        boolean isActive = command.isActive();

        final var category =
                this.categoryGateway.findById(id).orElseThrow(() -> DomainException.with(new Error("Category with ID %s was not found".formatted(id.getValue()))));

        /* Estamos criando um "container" para armazenar os possíveis erros que podem surgir do método "update()", seguindo o
        * "NotificationPattern". */
        final var notification = Notification.create();

        //Estamos fazendo a atualização do objeto de domínio.
        category.update(name, description, isActive).validate(notification);

        //Se nenhum erro ocorreu, então chamamos o método que salvará o objeto atualizado no banco de dados.
        return notification.hasError() ? Either.left(notification) : update(category);
    }

    //Se um erro ocorrer, ele será adicionado ao "Notification" e retornado. Caso contrário, retornará um "UpdateCategoryOutput".
    private Either<Notification, UpdateCategoryOutput> update(final Category category) {
        return Try(() -> this.categoryGateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }
}
