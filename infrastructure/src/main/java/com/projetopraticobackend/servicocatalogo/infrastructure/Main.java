package com.projetopraticobackend.servicocatalogo.infrastructure;

import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryRepository;
import com.projetopraticobackend.servicocatalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

@SpringBootApplication //Essa é a anotação que faz com que o Spring Boot saiba que essa é a classe principal da aplicação, além de configurar o Spring Boot com várias configurações iniciais.
public class Main {
    public static void main(String[] args) {

        System.out.println("Iniciando a aplicação...");
//        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development"); //Estamos definindo o perfil de execução da aplicação. Nesse caso, estamos definindo que a aplicação será executada em ambiente de desenvolvimento.
//        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "test"); //Estamos definindo o perfil de execução da aplicação. Nesse caso, estamos definindo que a aplicação será executada em ambiente de testes.

        //A "WebServerConfig" é a classe que contém as configurações do servidor web que o Spring subirá.
        SpringApplication.run(WebServerConfig.class, args);
    }

    /* O código abaixo será executado depois que o contexto do Spring subir, e o Spring nos dará uma
    * instância do "CategoryRepository". */
//    @Bean
//    public ApplicationRunner runner(CategoryRepository repository){
//        return args -> {
//            List<CategoryJpaEntity> all = repository.findAll();
//
//            Category filmes = Category.newCategory("Filmes", "Filmes de todos os gêneros.", true);
//
//            repository.saveAndFlush(CategoryJpaEntity.from(filmes));
//
//            repository.deleteAll();
//        };
//    }

//    @Bean
//    @DependsOnDatabaseInitialization //O Spring fará a injeção desse usecase. Esse método será executado apenas após a inicialização do banco de dados.
//    public ApplicationRunner runner(CreateCategoryUseCase createCategoryUseCase){
//        return args -> {
//
//        };
//    }
}