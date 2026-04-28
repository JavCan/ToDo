package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Category;
import com.itesm.infrastructure.persistence.entity.CategoryEntity;

public class CategoryMapper {
    public static CategoryEntity toEntity(Category category) {
        if (category == null) return null;
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.getUuid());
        entity.setName(category.getName());
        entity.setCreatedAt(category.getCreatedAt());
        return entity;
    }

    public static Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;
        Category category = new Category();
        category.setUuid(entity.getId());
        category.setName(entity.getName());
        category.setCreatedAt(entity.getCreatedAt());
        return category;
    }
}
