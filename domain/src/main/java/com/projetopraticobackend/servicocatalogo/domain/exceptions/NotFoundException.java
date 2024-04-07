package com.projetopraticobackend.servicocatalogo.domain.exceptions;

import com.projetopraticobackend.servicocatalogo.domain.AgregateRoot;
import com.projetopraticobackend.servicocatalogo.domain.validation.Error;
import com.projetopraticobackend.servicocatalogo.domain.valueobjects.Identifier;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/* Essa exception será lançada quando um agregado não for encontrado. */
public class NotFoundException extends DomainException {

    protected NotFoundException(final String message,
                                final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(
            final Class<? extends AgregateRoot<?>> aggregate, //Será a classe do agregado que não foi encontrado. Ela deverá herdar de "aggregateroot".
            final Identifier id
    ){

        final var error = "%s with ID %s was not found".formatted(aggregate.getSimpleName(),
                id.getValue());

        return new NotFoundException(error, Collections.emptyList());
    }
}
