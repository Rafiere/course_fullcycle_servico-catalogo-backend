package com.projetopraticobackend.servicocatalogo.infrastructure.api.controllers;

import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryCommand;
import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryOutput;
import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.update.UpdateCategoryCommand;
import com.projetopraticobackend.servicocatalogo.application.category.update.UpdateCategoryOutput;
import com.projetopraticobackend.servicocatalogo.application.category.update.UpdateCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.domain.pagination.Pagination;
import com.projetopraticobackend.servicocatalogo.domain.validation.handler.Notification;
import com.projetopraticobackend.servicocatalogo.infrastructure.api.CategoryAPI;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.models.CategoryApiOutput;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.models.UpdateCategoryApiInput;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    //Injetaremos os casos de uso que serão chamados pelo controller.
    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase,
                              final GetCategoryByIdUseCase getCategoryByIdUseCase,
                              final UpdateCategoryUseCase updateCategoryUseCase) {

        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInput createCategoryApiInput) {

        final var command = CreateCategoryCommand.with(
                createCategoryApiInput.name(),
                createCategoryApiInput.description(),
                createCategoryApiInput.active() != null ? createCategoryApiInput.active() : true); //O "Boolean" aceita um "null", portanto temos que realizar a tratativa de "!= null".


        /* No "Fold", o "Left" é o objeto que tem o "Notification", que é quando algo deu errado
        * na API, e o "Right" é o output. */

        /* Se um erro acontecer, será enviado o "notification" com o status 422. */
        final Function<Notification, ResponseEntity<?>> onError =
            notification -> ResponseEntity.unprocessableEntity().body(notification);

        /* Se tudo ocorrer bem, será enviado o "output" com o status 201 e o cabeçalho "Location". */
        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess =
            output -> ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        /* O lado da esquerda será se algo errado acontecer, e o lado da direita será se tudo ocorrer bem. */
        return createCategoryUseCase.execute(command)
                .fold(onError, onSuccess);
    }

    @Override
    public CategoryApiOutput getById(final String id) {
        return CategoryApiPresenter.present(getCategoryByIdUseCase.execute(id));

        //Ou usando o "apply()" para aplicar a função.
        //TODO: Estudar sobre o "apply" e o "compose" do Java.
//        return CategoryApiPresenter.present.compose(getCategoryByIdUseCase::execute).apply(id);
    }

    @Override
    public ResponseEntity<?> updateById(final String id,
                                        final UpdateCategoryApiInput input) {

        final var command = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active());

        final Function<Notification, ResponseEntity<?>> onError =
                notification -> ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;

        return updateCategoryUseCase.execute(command)
                .fold(onError, onSuccess);
    }


    @Override
    public Pagination<?> listCategories(String name, Integer page, Integer perPage, String sort, String direction) {
        return null;
    }
}
