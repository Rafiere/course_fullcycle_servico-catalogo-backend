package com.projetopraticobackend.servicocatalogo.infrastructure.api.controllers;

import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryCommand;
import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryOutput;
import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.projetopraticobackend.servicocatalogo.domain.pagination.Pagination;
import com.projetopraticobackend.servicocatalogo.domain.validation.handler.Notification;
import com.projetopraticobackend.servicocatalogo.infrastructure.api.CategoryAPI;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.models.CategoryAPIOutput;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.models.CreateCategoryApiInput;
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

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase,
                              final GetCategoryByIdUseCase getCategoryByIdUseCase) {

        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInput createCategoryApiInput) {

        final var command = CreateCategoryCommand.with(
                createCategoryApiInput.name(),
                createCategoryApiInput.description(),
                createCategoryApiInput.active() != null ? createCategoryApiInput.active() : true);


        /* No "Fold", o "Left" é o objeto que tem o "Notification", que é quando algo deu errado
        * na API, e o "Right" é o output. */

        /* Se um erro acontecer, será enviado o "notification" com o status 422. */
        final Function<Notification, ResponseEntity<?>> onError =
            notification -> ResponseEntity.unprocessableEntity().body(notification);

        /* Se tudo ocorrer bem, será enviado o "output" com o status 201 e o cabeçalho "Location". */
        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess =
            output -> ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return createCategoryUseCase.execute(command)
                .fold(onError, onSuccess);
    }

    @Override
    public CategoryAPIOutput getById(final String id) {
        return CategoryApiPresenter.present(getCategoryByIdUseCase.execute(id));

        //Ou usando o "apply()" para aplicar a função.
        //TODO: Estudar sobre o "apply" e o "compose" do Java.
//        return CategoryApiPresenter.present.compose(getCategoryByIdUseCase::execute).apply(id);
    }

    @Override
    public Pagination<?> listCategories(String name, Integer page, Integer perPage, String sort, String direction) {
        return null;
    }
}
