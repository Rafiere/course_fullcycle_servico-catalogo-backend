package com.projetopraticobackend.servicocatalogo.application;


import com.projetopraticobackend.servicocatalogo.domain.category.Category;

/* Por padrão, o caso de uso receberá algo e retornará algo. Nem sempre isso
* acontecerá, porém, esse caso de uso representará esse tipo. */
public abstract class UseCase<IN, OUT> {

    /* Por padrão, os casos de uso implementam o padrão "command()". Na
    * literatura, eles têm um único método público, que é chamado de "execute()". */
    public abstract OUT execute(IN in);
}