package com.projetopraticobackend.servicocatalogo;


import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@WebMvcTest //Essa anotação é semanticamente equivalente ao "@DataJpaTest", mas não tem o mesmo comportamento.
            //Ela serve para configurar apenas o que é controllers e o que é responsável pela serialização e desserialização de objetos.
public @interface ControllerTest {

    /* Estamos definindo que a anotação "@ControllerTest" vai receber como parâmetro um
    * controller e o Spring fará o binding dessa anotação e passará para a propriedade "controllers" do "@WebMvcTest", assim, carregaremos apenas o contexto dos controllers que forem
    * passados para essa anotação. */
    @AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
    Class<?>[] controllers() default {};
}
