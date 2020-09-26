package com.goodgold.logistics.repository;

import com.goodgold.logistics.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Category findCategoryByName(String name);
}
