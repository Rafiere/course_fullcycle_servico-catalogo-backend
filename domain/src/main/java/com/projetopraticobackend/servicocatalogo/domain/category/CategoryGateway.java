package com.projetopraticobackend.servicocatalogo.domain.category;

import com.projetopraticobackend.servicocatalogo.domain.pagination.Pagination;

import java.util.Optional;

/* Essa interface fará a exposição do "Category" para os outros casos de
* uso. Assim, os casos de uso apenas poderão manipular essa entidade por
* meio desses parâmetros. */
public interface CategoryGateway {

    /* Esse método criará essa categoria. */
    Category create(Category category);

    /* Esse método excluirá essa categoria. */
    void deleteById(CategoryID id);

    /* Esse método buscará por essa categoria */
    Optional<Category> findById(CategoryID id);

    /* Esse método atualizará essa categoria. */
    Category update(Category category);

    /* Esse método buscará por todas as categorias. Ele receberá um
    * parâmetro que servirá para que o cliente, ou seja, quem está consumindo
    * esse gateway, informe as categorias que ele deseja. */
    Pagination<Category> findAll(CategorySearchQuery query);
}
