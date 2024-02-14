package com.projetopraticobackend.servicocatalogo.domain.validation.handler;

import com.projetopraticobackend.servicocatalogo.domain.exceptions.DomainException;
import com.projetopraticobackend.servicocatalogo.domain.validation.Error;
import com.projetopraticobackend.servicocatalogo.domain.validation.ValidationHandler;

import java.util.List;

/* Esse validador NÃO implementa o "Notification Pattern", como o "NotificationHandler". Ele, pelo
* contrário, lançará uma exception sempre que receber um erro, ou seja, os erros não serão cumulativos. */
public class ThrowsValidationHandler implements ValidationHandler {


    @Override
    public ValidationHandler append(final Error error) {
        throw DomainException.with(error);
    }

    @Override
    public ValidationHandler append(ValidationHandler handler) {
        throw DomainException.with(handler.getErrors());
    }

    @Override
    public ValidationHandler validate(Validation validation) {
        try {
            /* Se ocorrer um erro, esse Handler lançará o erro automaticamente. */
            validation.validate();
        } catch (final Exception ex){
            throw DomainException.with(List.of(new Error(ex.getMessage())));
        }

        return this;
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}