package com.projetopraticobackend.servicocatalogo.application.category.update;

import com.projetopraticobackend.servicocatalogo.application.UseCase;
import com.projetopraticobackend.servicocatalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {


}
