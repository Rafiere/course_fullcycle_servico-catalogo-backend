package com.projetopraticobackend.servicocatalogo.domain.validation;

public abstract class Validator {

    /* Será o "Handler" utilizado para manipular os erros. Poderá ser, por
    * exemplo, um Handler que implementa o "Notification Pattern" ou outro tipo
    * de handler. */
    private final ValidationHandler validationHandler;

    protected Validator(ValidationHandler validationHandler){
        this.validationHandler = validationHandler;
    }

    protected ValidationHandler validationHandler() {
        return validationHandler;
    }

    public abstract void validate();
}