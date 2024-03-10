package com.projetopraticobackend.servicocatalogo.application.category.create;

import com.projetopraticobackend.servicocatalogo.application.UseCase;

/* Aqui temos a abstração do caso de uso de "CreateCategoryUseCase". Isso é usado pois
* não devemos acoplar o nosso código em uma implementação, e sim em uma abstração. Assim, principalmente
* em projetos grandes, no futuro, podemos ter um novo "CreateCategory" que será diferente, assim, não
* mudaremos a interface em todos os lugares que usam esse caso de uso, bastando apenas criarmos outra
* implementação. */
public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, CreateCategoryOutput> {

}
