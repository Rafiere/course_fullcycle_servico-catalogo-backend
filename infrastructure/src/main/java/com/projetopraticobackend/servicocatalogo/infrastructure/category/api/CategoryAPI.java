package com.projetopraticobackend.servicocatalogo.infrastructure.category.api;

/* Aqui teremos a documentação com o Spring Docs e os métodos que a API de "Category" fará a
 * exposição. */

import com.projetopraticobackend.servicocatalogo.domain.pagination.Pagination;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/* É recomendado deixarmos a documentação e as definições da API em uma interface e
* as implementações do controller direto no controller. Isso é feito para não poluirmos muito
* a classe "Controller" original. */

@RequestMapping("/categories")
public interface CategoryAPI {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createCategory();

    @GetMapping
    Pagination<?> listCategories(@RequestParam(name = "search", required = false, defaultValue = "") final String name,
                                 @RequestParam(name = "page", required = false, defaultValue = "0") final Integer page, //Se o usuário não enviar nenhuma página padrão, ela será "0".
                                 @RequestParam(name = "perPage", required = false, defaultValue = "10") final Integer perPage, //O Spring fará a conversão do "defaultValue" automaticamente, se necessário.
                                 @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
                                 @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction);
}