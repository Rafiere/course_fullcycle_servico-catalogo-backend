package com.projetopraticobackend.servicocatalogo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

//Essa é uma extensão, que adicionaremos aos testes que necessitarem, que realizará o "cleanUp" do
//banco de dados para cada teste de integração.

//Essa extensão personalizada serve para limpar o banco de dados antes de cada teste, pegando todos
//os beans que são do tipo "Repository" e executando o método "deleteAll" deles.
public class CleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) {
        final var repositories = SpringExtension.getApplicationContext(context)
                .getBeansOfType(CrudRepository.class)
                .values(); //Estamos pegando todos os beans do tipo "Repository"

        cleanUp(repositories);
    }

    private void cleanUp(final Collection<CrudRepository> repositories){

        repositories.forEach(CrudRepository::deleteAll); //Vamos executar o método "deleteAll" para todos os repositórios que forem encontrados.
    }
}
