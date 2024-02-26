package com.projetopraticobackend.servicocatalogo.domain.category;

/* Esses são os parâmetros que qualquer consumidor poderá utilizar para fazer a busca dos
* objetos do tipo "Category" que estão no banco de dados. */
public record CategorySearchQuery(
        int page, //A página que a pessoa quer acessar.
        int perPage, //A quantidade de elementos por página que a pessoa quer receber.
        String terms, //Os termos que a pessoa quer buscar, como "Aventura".
        String sort, //O atributo que a pessoa quer usar para ordenar.
        String direction //Se a pessoa quer ordenar de forma ascendente ou descendente.
) {
}
