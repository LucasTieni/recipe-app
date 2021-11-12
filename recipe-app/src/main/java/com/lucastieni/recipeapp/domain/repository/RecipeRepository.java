package com.lucastieni.recipeapp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucastieni.recipeapp.domain.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long>{

}
