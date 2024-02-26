package com.projetopraticobackend.servicocatalogo.domain.pagination;

import java.util.List;

/* Esse "Pagination" poderá ser usado para qualquer entidade, como um "Category", um
 * "Genre" e etc. */
public record Pagination<T>(int currentPage, //É a página atual em que a paginação pertence.
                            int perPage, //Quantos registros temos por página.
                            int totalElements, //Quantas informações temos no banco de dados.
                            List<T> elements //Os itens que serão retornados na página atual.
) {
}
