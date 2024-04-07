package com.projetopraticobackend.servicocatalogo.infrastructure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetopraticobackend.servicocatalogo.ControllerTest;
import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryOutput;
import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.domain.exceptions.DomainException;
import com.projetopraticobackend.servicocatalogo.domain.validation.Error;
import com.projetopraticobackend.servicocatalogo.domain.validation.handler.Notification;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.models.CreateCategoryApiInput;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

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
                .thenReturn(API.Right(CreateCategoryOutput.from("123")));

        //Vamos utilizar o "mockMvc" para simularmos um "POST" para o controller.

        //Estamos fazendo um "POST" para a URL "/categories" passando o "command" como JSON.
        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(input)) //É o JSON que estamos enviando.
                .accept("application/json");

        //Estamos realizando uma requisição HTTP real para o controller e validando o status retornado e o header "Location".
        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print()) //Aqui temos vários predicados para validarmos as respostas.
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/categories/123"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo("123"))); //Estamos validando se o campo "id" do JSON retornado é "123".

        /* Estamos garantindo que o caso de uso foi chamado apenas uma única vez e que o argumento que foi passado para o
        * caso de uso está correto, ou seja, que o controller converteu corretamente a request em um command para o
        * caso de uso. */
        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(
                command -> command.name().equals(expectedName) &&
                           command.description().equals(expectedDescription) &&
                           command.isActive() == expectedIsActive
        ));
    }

    //Abaixo, temos o cenário triste.
    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";

        final var input = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        //Estamos criando a implementação do mock do UseCase, pois não o testaremos, e sim faremos o teste integrado
        //do controller. Quando esse caso de uso for chamado, ele retornará um erro.
        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedErrorMessage))));

        //Vamos utilizar o "mockMvc" para simularmos um "POST" para o controller.

        //Estamos fazendo um "POST" para a URL "/categories" passando o "command" como JSON.
        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(input)) //É o JSON que estamos enviando.
                .accept("application/json");

        //Estamos realizando uma requisição HTTP real para o controller e validando o status retornado e o header "Location".
        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print()) //Aqui temos vários predicados para validarmos as respostas.
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage))); //Estamos validando se o campo "message" do JSON retornado é uma mensagem de erro.

        /* Estamos garantindo que o caso de uso foi chamado apenas uma única vez e que o argumento que foi passado para o
         * caso de uso está correto, ou seja, que o controller converteu corretamente a request em um command para o
         * caso de uso. */
        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(
                command -> Objects.equals(command.name(), expectedName) &&
                           Objects.equals(command.description(), expectedDescription) &&
                           command.isActive() == expectedIsActive
        ));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";

        final var input = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(input));

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(
                command -> Objects.equals(command.name(), expectedName) &&
                           Objects.equals(command.description(), expectedDescription) &&
                           command.isActive() == expectedIsActive
        ));
    }
}