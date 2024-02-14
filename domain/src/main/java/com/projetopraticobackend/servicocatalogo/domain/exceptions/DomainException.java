package com.projetopraticobackend.servicocatalogo.domain.exceptions;


import com.projetopraticobackend.servicocatalogo.domain.validation.Error;

import java.util.List;

public class DomainException extends RuntimeException {

    private final List<Error> errors;

    public DomainException(final List<Error> errors){
        super("", null, true, false); //Os dois últimos parâmetros são para que a stacktrace não seja impressa de forma completa. Eles auxiliam a performance da aplicação.
        this.errors = errors;
    }

    public static DomainException with(final List<Error> errors){
        return new DomainException(errors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}