package com.projetopraticobackend.servicocatalogo.application.category.create;

import com.projetopraticobackend.servicocatalogo.application.UseCase;
import com.projetopraticobackend.servicocatalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

/* Aqui temos a abstração do caso de uso de "CreateCategoryUseCase". Isso é usado pois
* não devemos acoplar o nosso código em uma implementação, e sim em uma abstração. Assim, principalmente
* em projetos grandes, no futuro, podemos ter um novo "CreateCategory" que será diferente, assim, não
* mudaremos a interface em todos os lugares que usam esse caso de uso, bastando apenas criarmos outra
* implementação. */

/* O caso de uso receberá um "command" e o retorno será ou um objeto do tipo "Notification", se existirem erros durante a
* execução do caso de uso, ou um "CreateCategoryOutput", se a operação for concluída com sucesso. */
public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
