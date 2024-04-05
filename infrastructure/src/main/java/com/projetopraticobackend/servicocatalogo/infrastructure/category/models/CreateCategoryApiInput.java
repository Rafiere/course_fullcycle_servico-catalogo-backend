package com.projetopraticobackend.servicocatalogo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/* Essa é como se fosse uma classe "Request". Ela serve para
* que o front-end envie os dados nesse formato. */

/* Poderíamos configurar diretamente o ObjectMapper, mas as record ainda não estão
* funcionando perfeitamente com o ObjectMapper. */
public record CreateCategoryApiInput(@JsonProperty("name") String name,
                                     @JsonProperty("description") String description,
                                     @JsonProperty("is_active") Boolean active) {
}
