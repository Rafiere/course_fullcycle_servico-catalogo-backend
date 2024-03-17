package com.projetopraticobackend.servicocatalogo.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.projetopraticobackend.servicocatalogo") //Estamos indicando qual será o pacote básico em que o Spring procurará por classes para serem gerenciadas por ele. Nesse caso, é a raiz do projeto.
public class WebServerConfig {
}