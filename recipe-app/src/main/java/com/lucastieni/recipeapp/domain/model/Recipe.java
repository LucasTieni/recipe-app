package com.lucastieni.recipeapp.domain.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String Title;
	
	@Enumerated(value = EnumType.STRING)
	private MealType mealType;
	
	private Integer servedPeople;
	
	@Enumerated(value = EnumType.STRING)
	private DifficultyLevel difficultyLevel;
	
	private String ingredients; 
	
	private String instructions;


}
