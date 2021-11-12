package com.lucastieni.recipeapp.domain.service;

import java.util.List;
import java.util.Optional;

import com.lucastieni.recipeapp.domain.model.Recipe;

public interface RecipeService {
	
	Recipe salvar( Recipe recipe);
	
	Recipe atualizar( Recipe recipe);
	
	void deletar( Recipe recipe);
	
	List<Recipe> findAll ();
	
	List<Recipe> search ( Recipe recipeFilter);
	
	void validar(Recipe recipe);
	
	Optional<Recipe> obterPorId(Long id);
}
