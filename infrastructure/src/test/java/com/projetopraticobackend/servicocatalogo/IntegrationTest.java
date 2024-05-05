package com.projetopraticobackend.servicocatalogo;

import com.projetopraticobackend.servicocatalogo.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

/* Essa anotação servirá para configurarmos os testes de integração da aplicação.
*  Estamos subindo o "@SpringBootTest" passando a classe "WebServerConfig" para que ele
*  use a mesma classe que a aplicação real usa para configurar o servidor. */

/* Quando a aplicação for rodada em uma classe com essa anotação, o "profile" utilizado será
*  o profile de "test". */

/* Para esses testes de integração de casos de uso e dos repositórios, não utilizaremos mais
* os "mocks" do Mockito, e sim mocks reais, ou seja, persistiremos as informações no banco de
* dados em memória e interagiremos com ela através dos "usecases". */

/* Essa anotação subirá todo o contexto do Spring. */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@ExtendWith(CleanUpExtension.class)
@SpringBootTest(classes = WebServerConfig.class) //Essa é uma classe global de testes do Spring. O parâmetro "classes" permite que passemos uma classe de configuração com metadados para ele entender como quais configurações do Spring Boot ele precisa habilitar.
public @interface IntegrationTest {
}