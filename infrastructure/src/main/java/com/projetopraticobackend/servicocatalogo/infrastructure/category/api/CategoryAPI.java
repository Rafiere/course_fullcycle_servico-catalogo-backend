package com.projetopraticobackend.servicocatalogo.infrastructure.category.api;

/* Aqui teremos a documentação com o Spring Docs e os métodos que a API de "Category" fará a
 * exposição. */

import com.projetopraticobackend.servicocatalogo.domain.pagination.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/* É recomendado deixarmos a documentação e as definições da API em uma interface e
* as implementações do controller direto no controller. Isso é feito para não poluirmos muito
* a classe "Controller" original. */

@RequestMapping("/categories")
@Tag(name = "Categories") //É o nome do "resource" que estamos expondo.
public interface CategoryAPI {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new category") //Aqui temos uma descrição do que o endpoint faz.
    @ApiResponses(value = { //Aqui temos as possíveis respostas desse endpoint.
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> createCategory();

    @GetMapping
    @Operation(summary = "List all categories paginated")
    @ApiResponses(value = { //Aqui temos as possíveis respostas desse endpoint.
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "An invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    Pagination<?> listCategories(@RequestParam(name = "search", required = false, defaultValue = "") final String name,
                                 @RequestParam(name = "page", required = false, defaultValue = "0") final Integer page, //Se o usuário não enviar nenhuma página padrão, ela será "0".
                                 @RequestParam(name = "perPage", required = false, defaultValue = "10") final Integer perPage, //O Spring fará a conversão do "defaultValue" automaticamente, se necessário.
                                 @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
                                 @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction);
}