package com.projetopraticobackend.servicocatalogo.infrastructure.configuration.usecases;

import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.delete.DefaultGetCategoryByIdUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.delete.DeleteCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.update.DefaultUpdateCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.application.category.update.UpdateCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/* A camada de application não é reconhecida pelo Spring, pois não estamos utilizando
* nenhuma anotação como "@Component" e etc, já que o objetivo dessa camada é ficar separada da
* camada de infraestrutura, e apenas na camada de infraestrutura que podemos utilizar as anotações do
* Spring. */

/* Dessa forma, nessa classe de configuração, faremos a configuração de todos os "Beans" que são pertinentes ao
* caso de uso. */

/* Dentro dessa classe, temos as configurações de injeção de dependências de
* "Category". */

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway; //Estamos no módulo de infraestrutura, então o Spring fará a injeção aqui. Esse gateway será necessário em todos os casos de uso.

    public CategoryUseCaseConfig(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    //Abaixo, vamos criar um "Bean" para cada caso de uso, para
    //que ele possa ser injetado e reconhecido pelo Spring, sem
    //que seja necessário utilizar anotações do Spring na camada
    //de application.

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(){
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(){
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase(){
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase(){
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase(){
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }
}