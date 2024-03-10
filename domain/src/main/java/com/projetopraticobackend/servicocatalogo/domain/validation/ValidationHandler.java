package com.projetopraticobackend.servicocatalogo.domain.validation;

import java.util.List;

/* Essa é uma interface fluente. Podemos chamar um método que retorna a própria
* instância e encadear métodos. */
public interface ValidationHandler {

    /* Esse método fará o "append()" de um erro existente. */
    ValidationHandler append(Error error);

    /* Poderemos receber outros erros de um outro handler e incrementar no handler
    * atual, como se fosse um "addAll()" de erros. */
    ValidationHandler append(ValidationHandler handler);

    /* Esse método permitirá lançarmos uma exception. */
    ValidationHandler validate(Validation validation);

    default boolean hasError(){
        return getErrors() != null && !(getErrors().isEmpty());
    }

    default Error firstError(){
        if(getErrors() != null && !getErrors().isEmpty()){
            return getErrors().get(0);
        } else {
            return null;
        }
    }

    /* Esse método retornará todos os erros do Handler. */
    List<Error> getErrors();

    /* Essa interface permitirá passarmos algum método que lance alguma
    * exception. */
    public interface Validation {
        void validate();
    }
}