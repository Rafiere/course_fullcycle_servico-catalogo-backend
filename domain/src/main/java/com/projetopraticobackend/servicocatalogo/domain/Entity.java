package com.projetopraticobackend.servicocatalogo.domain;

import com.projetopraticobackend.servicocatalogo.domain.validation.ValidationHandler;
import com.projetopraticobackend.servicocatalogo.domain.valueobjects.Identifier;

import java.util.Objects;

/* Uma entidade é algo definido pelo seu ID. */
public abstract class Entity<ID extends Identifier> {

    protected final ID id;

    protected Entity(final ID id){

        Objects.requireNonNull(id, "'id' should not be null");
        this.id = id;
    }

    /* Todas as entidades, segundo o DDD, devem saber se validar. */
    /* Cada entidade poderá utilizar um "Handler" diferente, e esse "Handler" seguirá
    * a interface. */
    /* Inicialmente, utilizaremos o "NotificationHandler", que é um "Handler" responsável
    * por acumular os erros, porém, posteriormente, podemos utilizar outro handler, que lança
    * a exception assim que o erro ocorrer, por exemplo. */
    public abstract void validate(ValidationHandler handler);

    public ID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; //Verifica se a referência do "this" é a mesma referência do objeto.
        if (o == null || getClass() != o.getClass()) return false; //Verifica se o objeto é nulo ou se a classe do objeto é diferente da classe do "this".
        Entity<?> entity = (Entity<?>) o; //Faz um cast do objeto para "Entity".
        return Objects.equals(getId(), entity.getId()); //Verifica se o ID do objeto é igual ao ID do "this". Se for igual, os objetos serão iguais.
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}