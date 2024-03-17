package com.projetopraticobackend.servicocatalogo.infrastructure;

import com.projetopraticobackend.servicocatalogo.application.UseCase;
import com.projetopraticobackend.servicocatalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //Essa é a anotação que faz com que o Spring Boot saiba que essa é a classe principal da aplicação, além de configurar o Spring Boot com várias configurações iniciais.
public class Main {
    public static void main(String[] args) {

        System.out.println("Iniciando a aplicação...");

        //A "WebServerConfig" é a classe que contém as configurações do servidor web que o Spring subirá.
        SpringApplication.run(WebServerConfig.class, args);
    }
}