package com.projetopraticobackend.servicocatalogo.domain;

import com.projetopraticobackend.servicocatalogo.domain.valueobjects.Identifier;

/* O "AgregateRoot" será o agregado raiz. Eles serão as únicas classes que serão
* persistidas. Ele é um tipo especializado de entidade, pois uma entidade é definida pelo
* ID, mas ela nem sempre é persistida, ou seja, terá um repositório para ela, apenas se ela
* for do tipo "AggregateRoot". */
public abstract class AgregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AgregateRoot(final ID id) {
        super(id);
    }
}