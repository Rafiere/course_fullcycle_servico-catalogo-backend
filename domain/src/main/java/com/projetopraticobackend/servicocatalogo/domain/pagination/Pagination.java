package com.projetopraticobackend.servicocatalogo.domain.pagination;

import java.util.List;
import java.util.function.Function;

/* Esse "Pagination" poderá ser usado para qualquer entidade, como um "Category", um
 * "Genre" e etc. */
public record Pagination<T>(int currentPage, //É a página atual em que a paginação pertence.
                            int perPage, //Quantos registros temos por página.
                            long totalElements, //Quantas informações temos no banco de dados.
                            List<T> elements //Os itens que serão retornados na página atual.
) {

    /* O "R" é o tipo do resultado, e o "T" é o tipo que ele tem. */
    public <R> Pagination<R> map(final Function<T, R> mapper) {

        final List<R> newList = this.elements.stream()
                .map(mapper)
                .toList();

        return new Pagination<>(currentPage(), perPage(), totalElements(), newList);
    }
}
