package com.lucastieni.recipeapp.domain.dto;

import com.lucastieni.recipeapp.domain.model.Recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {
	private Long id;
	private String title;
	private String mealType;
	private Integer servedPeople;
	private String difficultyLevel;
	private String ingredients; 
	private String instructions;
	
	public static RecipeDTO fromModel(Recipe recipe) {
		return new RecipeDTO(recipe.getId(), 
				recipe.getTitle() , 
				recipe.getMealType().toString(),
				recipe.getServedPeople(),
				recipe.getDifficultyLevel().toString(),
				recipe.getIngredients(),
				recipe.getInstructions()
		);
	}
}
