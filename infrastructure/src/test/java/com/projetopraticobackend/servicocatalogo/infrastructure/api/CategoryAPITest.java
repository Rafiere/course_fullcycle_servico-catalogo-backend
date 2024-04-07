package com.projetopraticobackend.servicocatalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetopraticobackend.servicocatalogo.ControllerTest;
import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryOutput;
import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.retrieve.get.CategoryOutput;
import com.projetopraticobackend.servicocatalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;
import com.projetopraticobackend.servicocatalogo.domain.exceptions.DomainException;
import com.projetopraticobackend.servicocatalogo.domain.exceptions.NotFoundException;
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

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

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

        //Given - É tudo o que é setup do teste.
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";

        final var input = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        //When - Ele acaba quando chamamos o método de execução do teste.
        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(input));

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //Then - São todas as verificações.
        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
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

    /* Abaixo, temos o teste de integração do endpoint para buscar uma categoria pelo ID. */
    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception {

        //Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = category.getId();

        /* Estamos mockando a resposta do caso de uso, para simular que ele foi chamado, pois esse é
         * um teste de integração apenas do controller, ou seja, estamos chamando apenas o controller através
         * de chamadas HTTP. */
        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenReturn(CategoryOutput.from(category));

        //When
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue())
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());


        //Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(expectedDescription)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(category.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(category.getUpdatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted_at", Matchers.nullValue()));

        Mockito.verify(getCategoryByIdUseCase, Mockito.times(1)).execute(Mockito.eq(expectedId.getValue()));
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
        //Given
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");

        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        //When
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
    }
}