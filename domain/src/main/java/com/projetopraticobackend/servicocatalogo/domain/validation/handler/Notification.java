package com.projetopraticobackend.servicocatalogo.domain.validation.handler;

import com.projetopraticobackend.servicocatalogo.domain.exceptions.DomainException;
import com.projetopraticobackend.servicocatalogo.domain.validation.Error;
import com.projetopraticobackend.servicocatalogo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

/* Esse padrão é uma outra forma de lidar com erros, proposta pelo Martin Fowler, e
* será utilizada em alguns lugares da aplicação. Assim, ao invés de, ao receber um
* erro, lançá-lo imediatamente, como ocorre com o "ThrowsValidationHandler", ele
* acumulará todos os erros e lançará de uma única vez. Ele é útil em cadastros, por
* exemplo. */
public class Notification implements ValidationHandler {

    private final List<Error> errors;

    private Notification(final List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create(){
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Error error){
        return new Notification(new ArrayList<>()).append(error);
    }

    @Override
    public Notification append(final Error error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public Notification validate(final Validation validation) {

        try {
            validation.validate();
        } catch (final DomainException ex){
            this.errors.addAll(ex.getErrors()); //O DomainException já possui erros dentro dele, por isso damos um tratamento especial para esse tipo de exception.
        } catch (final Throwable t){
            this.errors.add(new Error(t.getMessage()));
        }

        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}
