package com.projetopraticobackend.servicocatalogo.infrastructure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetopraticobackend.servicocatalogo.ControllerTest;
import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryCommand;
import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryOutput;
import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.retrieve.get.CategoryOutput;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.models.CreateCategoryApiInput;
import io.vavr.API;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/* Nessa classe, teremos os testes integrados dos controllers de criação de categoria. */

@ControllerTest(controllers = CategoryAPI.class)
//Apenas será feito o "scan" do controller "CategoryAPI". Os "components" e "services" não terão o scan realizado.
//Dessa forma, teremos que realizar o mock dos components e services.
public class CategoryAPITest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc; //Esse é um helper que nos auxiliará a fazer chamadas REST para os controllers que estamos passando na anotação.

    @MockBean
    //Estamos dizendo para o Spring que temos que criar uma versão desse bean totalmente mockada, porque, por padrão, o "@WebMvcTest" não carrega o contexto de "services" e "components".
    private CreateCategoryUseCase createCategoryUseCase;

    /* Esse será um teste de integração pois estamos fazendo a chamada HTTP real para o endpoint. Mesmo que estejamos
     * mockando o usecase, o controller está realmente sendo chamado, por isso temos um teste de integração. */
    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var input = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        //Estamos criando a implementação do mock do UseCase, pois não o testaremos, e sim faremos o teste integrado
        //do controller. Quando esse caso de uso for chamado, ele retornará uma categoria com o ID "123".
        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(CreateCategoryOutput.from(CategoryID.from("123"))));

        //Vamos utilizar o "mockMvc" para simularmos um "POST" para o controller.

        //Estamos fazendo um "POST" para a URL "/categories" passando o "command" como JSON.
        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input)) //É o JSON que estamos enviando.
                .accept("application/json");

        //Estamos realizando uma requisição HTTP real para o controller e validando o status retornado e o header "Location".
        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll( //Aqui passaremos uma lista de predicados para validarmos a resposta.
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.header().string("Location", "/categories/123")
                );

        /* Estamos garantindo que o caso de uso foi chamado apenas uma única vez e que o argumento que foi passado para o
        * caso de uso está correto, ou seja, que o controller converteu corretamente a request em um command para o
        * caso de uso. */
        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(
                command -> command.name().equals(expectedName) &&
                           command.description().equals(expectedDescription) &&
                           command.isActive() == expectedIsActive
        ));
    }
}