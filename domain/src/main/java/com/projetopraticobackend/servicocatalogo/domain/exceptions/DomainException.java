package com.projetopraticobackend.servicocatalogo.domain.exceptions;


import com.projetopraticobackend.servicocatalogo.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStacktraceException {

    private final List<Error> errors;

    public DomainException(final String message, final List<Error> errors){
        super("");
        this.errors = errors;
    }

    /* Esse método servirá para inserir um erro na lista de erros. */
    public static DomainException with(final Error error){
        return new DomainException(error.message(), List.of(error));
    }

    public static DomainException with(final List<Error> errors){
        return new DomainException("", errors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}