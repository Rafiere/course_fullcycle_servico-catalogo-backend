package com.projetopraticobackend.servicocatalogo.domain.category;

import com.projetopraticobackend.servicocatalogo.domain.valueobjects.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoryID extends Identifier {

    private final String value;

    private CategoryID(String value) {

        Objects.requireNonNull(value, "'value' should not be null");

        this.value = value;
    }

    /* Esse será o principal construtor para criarmos um "CategoryID". */
    public static CategoryID unique(){
        return from(UUID.randomUUID());
    }

    /* Esse construtor será usado para convertermos uma string em um ID. Por
    * exemplo, se tivermos ido ao banco de dados e nos foi retornado um "ID" no
    * formato de "string", utilizaremos o método abaixo para transformá-lo em um
    * "CategoryID". */
    public static CategoryID from(final String anId){
        return new CategoryID(anId);
    }

    /* Esse método será uma sobrecarga e transformará um "UUID" em um "CategoryID". */
    public static CategoryID from(final UUID anId){
        return new CategoryID(anId.toString().toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryID that = (CategoryID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String getValue() {
        return value;
    }
}