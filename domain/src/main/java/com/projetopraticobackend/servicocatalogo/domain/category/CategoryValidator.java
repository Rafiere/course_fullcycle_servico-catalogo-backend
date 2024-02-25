package com.projetopraticobackend.servicocatalogo.domain.category;

import com.projetopraticobackend.servicocatalogo.domain.validation.Error;
import com.projetopraticobackend.servicocatalogo.domain.validation.ValidationHandler;
import com.projetopraticobackend.servicocatalogo.domain.validation.Validator;

/* Os validadores tendem a mudar com uma maior frequência do que o domínio, dessa
* forma, criamos uma classe separada para ele. */
public class CategoryValidator extends Validator {

    /* A categoria abaixo será a categoria que será validada por esse validador. */
    private final Category category;

    public CategoryValidator(final Category category,
                             final ValidationHandler handler){

        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {

        checkNameConstraints();
    }

    private void checkNameConstraints() {

        final var name = this.category.getName();

        if(name == null){
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if(name.isBlank()){
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final int nameLength = name.trim().length();
        final int MAX_NAME_LENGTH = 255;
        final int MIN_NAME_LENGTH = 3;
        if(nameLength < MIN_NAME_LENGTH || nameLength > MAX_NAME_LENGTH){
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
