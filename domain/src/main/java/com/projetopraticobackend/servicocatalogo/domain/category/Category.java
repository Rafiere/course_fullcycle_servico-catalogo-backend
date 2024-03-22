package com.projetopraticobackend.servicocatalogo.domain.category;

import com.projetopraticobackend.servicocatalogo.domain.AgregateRoot;
import com.projetopraticobackend.servicocatalogo.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AgregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt; //A semântica do "Instant" é um marco no tempo, logo, ele calcula o tempo desde o início e conta os segundos até aqui. Ele é melhor do que o LocalDateTime pois ele é mais preciso e não depende do fuso horário, pois é sempre UTC.
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final CategoryID id,
                    final String name,
                    final String description,
                    final boolean active,
                    final Instant createdAt,
                    final Instant updatedAt,
                    final Instant deletedAt) {

        super(id);
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    //Esse método servirá como a "factory" para criar uma nova categoria.
    public static Category newCategory(final String name,
                                       final String description,
                                       final boolean active) {

        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedAt = active ? null : now;

        return new Category(id,
                name,
                description,
                active,
                now,
                now,
                deletedAt);
    }

    public static Category clone(final Category category) {
        return category.clone();
    }

    public static Category with(final CategoryID id,
                                final String name,
                                final String description,
                                final boolean active,
                                final Instant createdAt,
                                final Instant updatedAt,
                                final Instant deletedAt) {

        return Category.with(
                id,
                name,
                description,
                active,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Category activate(){
        if(getDeletedAt() != null){
            this.deletedAt = null;
        }

        this.active = true;
        this.updatedAt = Instant.now();

        return this;
    }

    public Category deactivate(){
        if(getDeletedAt() == null){ //Se estamos desativando uma categoria que já está desativada, não mudaremos a data de desativação.
            this.deletedAt = Instant.now();
        }

        this.active = false;
        this.updatedAt = Instant.now();

        return this;
    }

    public Category update(final String name,
                           final String description,
                           final boolean isActive){

        this.name = name;
        this.description = description;

        if (isActive) {
            activate();
        } else {
            deactivate();
        }

        this.updatedAt = Instant.now();

        return this;
    }

    /* Utilizaremos um validador externo, que é uma classe apenas para implementar
    * a validação da "Category". */
    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    /* O "clone()" é um comportamento da JVM que pega os atributos de um objeto, cria um novo objeto e
    * copia esses atributos para o novo objeto gerado, ou seja, criamos uma "cópia" do objeto, mas com referências
    * diferentes. */
    @Override
    protected Category clone() {
        try {

            //Se tivermos um atributo mutável, como uma lista, por exemplo, precisamos copiá-lo também, para que a cópia não possa mudar o estado interno do original.
            //Como, no objeto atual, não temos atributos mutáveis, não precisamos fazer nada.

            Category clone = (Category) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}