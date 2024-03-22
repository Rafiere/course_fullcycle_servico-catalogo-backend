package com.projetopraticobackend.servicocatalogo.infrastructure;

import com.projetopraticobackend.servicocatalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication //Essa é a anotação que faz com que o Spring Boot saiba que essa é a classe principal da aplicação, além de configurar o Spring Boot com várias configurações iniciais.
public class Main {
    public static void main(String[] args) {

        System.out.println("Iniciando a aplicação...");
//        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development"); //Estamos definindo o perfil de execução da aplicação. Nesse caso, estamos definindo que a aplicação será executada em ambiente de desenvolvimento.
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "test"); //Estamos definindo o perfil de execução da aplicação. Nesse caso, estamos definindo que a aplicação será executada em ambiente de desenvolvimento.

        //A "WebServerConfig" é a classe que contém as configurações do servidor web que o Spring subirá.
        SpringApplication.run(WebServerConfig.class, args);
    }
}