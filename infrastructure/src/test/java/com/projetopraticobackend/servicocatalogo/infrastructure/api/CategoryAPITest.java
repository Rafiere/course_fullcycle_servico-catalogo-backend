package com.projetopraticobackend.servicocatalogo.infrastructure.api;

import com.projetopraticobackend.servicocatalogo.ControllerTest;
import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.api.CategoryAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/* Nessa classe, teremos os testes integrados dos controllers de criação de categoria. */

@ControllerTest(controllers = CategoryAPI.class) //Apenas será feito o "scan" do controller "CategoryAPI". Os "components" e "services" não terão o scan realizado.
                                                 //Dessa forma, teremos que realizar o mock dos components e services.
public class CategoryAPITest {

    @Autowired
    private MockMvc mockMvc; //Esse é um helper que nos auxiliará a fazer chamadas REST para os controllers que estamos passando na anotação.

    @MockBean //Estamos dizendo para o Spring que temos que criar uma versão desse bean totalmente mockada, porque, por padrão, o "@WebMvcTest" não carrega o contexto de "services" e "components".
    private CreateCategoryUseCase createCategoryUseCase;

    @Test
    public void test(){

    }
}