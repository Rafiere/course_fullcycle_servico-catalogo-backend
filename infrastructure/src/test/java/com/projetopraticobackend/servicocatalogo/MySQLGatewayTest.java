package com.projetopraticobackend.servicocatalogo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;

/* Essa anotação subirá apenas o contexto pertinente a persistência. */

@Target(ElementType.TYPE) //Essa anotação será utilizada para definir onde essa anotação poderá ser utilizada.
@Retention(RetentionPolicy.RUNTIME) //Essa anotação será utilizada para definir até quando essa anotação estará disponível.
@Inherited //Essa anotação será utilizada para definir que essa anotação poderá ser herdada.
@ActiveProfiles("test") //Estamos definindo que utilizaremos o perfil "test" ao executarmos os testes.
//Essa anotação será utilizada pois ela não sobe o contexto inteiro do Spring. Ela apenas configura o contexto necessário para testarmos o repositório. O problema de usar essa anotação é que ele não enxerga o gateway que está anotado com o "@Service". Para fazer ele escanear, precisamos utilizar a anotação "@ComponentScan".
@ComponentScan(includeFilters = { //Com essa anotação, diremos para o Spring quem devemos escanear além do que foi escaneado pelo "DataJpaTest". Essa configuração será feita através de filtros.
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".[MySQLGateway]") //Esse filtro será feito através de "RegEx". Assim, o Spring escaneará, além de tudo o que foi configurado pelo "@DataJpaTest", todas as classes que terminarem com "MySQLGateway" para esses testes.
})
//@SpringBootTest //Essa anotação faz com que o Spring Boot suba o contexto da aplicação antes de rodar os testes. Esse contexto é recomendado apenas para testes end-to-end. Por isso, não utilizaremos essa anotação.
@DataJpaTest //Essa anotação faz com que o Spring Boot suba o contexto do JPA antes de rodar os testes. Esse contexto é recomendado para testes de integração. Por isso, utilizaremos essa anotação.
@ExtendWith(CleanUpExtension.class) //Estamos dizendo para o JUnit que queremos que a extensão "CleanUpExtensions" seja executada antes de cada teste.
public @interface MySQLGatewayTest {
}



