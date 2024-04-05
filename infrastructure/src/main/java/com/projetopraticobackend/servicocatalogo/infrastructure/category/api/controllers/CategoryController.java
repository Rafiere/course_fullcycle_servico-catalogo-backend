package com.projetopraticobackend.servicocatalogo.infrastructure.category.api.controllers;

import com.projetopraticobackend.servicocatalogo.domain.pagination.Pagination;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.api.CategoryAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryAPI {
    @Override
    public ResponseEntity<?> createCategory() {
        return null;
    }

    @Override
    public Pagination<?> listCategories(String name, Integer page, Integer perPage, String sort, String direction) {
        return null;
    }
}
