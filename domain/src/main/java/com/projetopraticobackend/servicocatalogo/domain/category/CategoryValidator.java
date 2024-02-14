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

        if(this.category.getName() == null){
            this.validationHandler().append(new Error("'name' should not be null"));
        }
    }
}
