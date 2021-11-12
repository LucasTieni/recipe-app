package com.lucastieni.recipeapp.domain.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucastieni.recipeapp.domain.exception.RegraNegocioException;
import com.lucastieni.recipeapp.domain.model.Recipe;
import com.lucastieni.recipeapp.domain.repository.RecipeRepository;
import com.lucastieni.recipeapp.domain.service.RecipeService;

@Service
public class RecipeServiceImpl implements RecipeService{
	
	private RecipeRepository recipeRepository;
	
	@Autowired
	public RecipeServiceImpl(RecipeRepository recipeRepository) {
//		super();
		this.recipeRepository = recipeRepository;
	}

	@Override
	@Transactional
	public Recipe salvar( Recipe recipe) {
		validar(recipe);
		return recipeRepository.save(recipe);
	}
	
	@Override
	@Transactional
	public Recipe atualizar(Recipe recipe) {
		Objects.requireNonNull(recipe.getId());
		validar(recipe);
		return recipeRepository.save(recipe);
	}
	
	@Override
	@Transactional
	public void deletar(Recipe recipe) {
		Objects.requireNonNull(recipe.getId());
		recipeRepository.delete(recipe);
	}
	
	@Override
	public Optional<Recipe> obterPorId(Long id) {
		return recipeRepository.findById(id);
	}
	
	@Override
	public List<Recipe> findAll(){
		return recipeRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Recipe> search ( Recipe recipeFilter) {
		Example example = Example.of( recipeFilter, ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING) );
		
		return recipeRepository.findAll(example);
	}
	
	
	@Override
	public void validar(Recipe recipe) {
		
		if(recipe.getTitle() == null || recipe.getTitle().trim().equals("")) {
			throw new RegraNegocioException("Informe um nome para a receita válido!");
		}
		
		if (recipe.getServedPeople() == null || recipe.getServedPeople().compareTo(0) < 1) {
			throw new RegraNegocioException("Informe um valor válido para quantidade de pessoas servidas!");
		}
		
		if(recipe.getIngredients() == null || recipe.getIngredients().trim().equals("")) {
			throw new RegraNegocioException("Informe os ingredientes!");
		}
		
		if(recipe.getTitle() == null || recipe.getTitle().trim().equals("")) {
			throw new RegraNegocioException("Informe as instruções de preparo!");
		}
		
		if (recipe.getMealType() == null) {
			throw new RegraNegocioException("Informe um tipo de refeição.");
		}
		
		if (recipe.getDifficultyLevel() == null) {
			throw new RegraNegocioException("Informe um nível de dificuldade.");
		}
	}
	
}
