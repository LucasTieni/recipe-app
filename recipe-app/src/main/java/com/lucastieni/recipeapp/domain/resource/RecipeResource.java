package com.lucastieni.recipeapp.domain.resource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucastieni.recipeapp.domain.dto.RecipeDTO;
import com.lucastieni.recipeapp.domain.exception.RegraNegocioException;
import com.lucastieni.recipeapp.domain.model.DifficultyLevel;
import com.lucastieni.recipeapp.domain.model.MealType;
import com.lucastieni.recipeapp.domain.model.Recipe;
import com.lucastieni.recipeapp.domain.service.RecipeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags="RecipeApp")
@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeResource {
	
	private final RecipeService recipeService;	
	
	@ApiOperation("Salva nova receita")
	@PostMapping
	public ResponseEntity salvar( @RequestBody RecipeDTO dto ) {
		try {
			Recipe recipe = convert(dto);
			
			Recipe savedRecipe = recipeService.salvar(recipe);
			return new ResponseEntity(savedRecipe, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@ApiOperation("Edita uma receita por id")
	@PutMapping("{id}")
	public ResponseEntity atualizar( @PathVariable("id") Long id, @RequestBody RecipeDTO dto) {
		return recipeService.obterPorId(id).map( entity -> {
			try {
				Recipe recipe = convert(dto);
				recipe.setId(entity.getId());
				recipeService.atualizar(recipe);
				return ResponseEntity.ok(recipe);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet( () -> new ResponseEntity("Recipe not found in Database.", HttpStatus.BAD_REQUEST));
	}
	
	@ApiOperation("Deleta uma receita")
	@DeleteMapping("{id}")
	public ResponseEntity deletar( @PathVariable("id") Long id) {
		return recipeService.obterPorId(id).map( entity -> {
			recipeService.deletar(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet( () -> new 
				ResponseEntity("Recipe not found in Database.", HttpStatus.BAD_REQUEST));
	}
	
	@ApiOperation("Lista todas receitas salvas")
	@GetMapping("/list")
	public List<RecipeDTO> getLista(){
		return recipeService.findAll().stream()
				.map( RecipeDTO::fromModel )
				.collect(Collectors.toList());
	}
	
	@ApiOperation("Busca uma receita por Id")
	@GetMapping("{id}")
	public ResponseEntity<RecipeDTO> getById(@PathVariable Long id){
		Optional<Recipe> recipeTemp = recipeService.obterPorId(id);
		
		if(recipeTemp.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		RecipeDTO recipe = recipeTemp.map(RecipeDTO::fromModel).get();
		return ResponseEntity.ok(recipe);
	}
	
	@ApiOperation("Busca receitas por título ou/e nível de diculdade ou/e pessoas servidas ou/e tipo de refeição")
	@GetMapping
	public ResponseEntity buscar(
		@ApiParam(value = "Nível de dificuldade da receita (BEGINNER / INTERMEDIATE / ADVANCED)") @RequestParam(value = "difficultyLevel", required = false) String difficultyLevel,
		@ApiParam(value = "Tipo de refeição ( BREAKFAST, LUNCH, SUPPER, SNACK)") @RequestParam(value = "mealType", required = false) String mealType,
		@ApiParam(value = "Pessoas servidas pela receita", example = "5") @RequestParam(value = "servedPeople", required = false) Integer servedPeople,
		@ApiParam(value = "Título da receita", example = "pizza")@RequestParam(value = "title", required = false) String title
		) {
		
		Recipe recipeFiltro = new Recipe();
		recipeFiltro.setTitle(title);
		recipeFiltro.setServedPeople(servedPeople);
		
		if (mealType != null) {
		recipeFiltro.setMealType(MealType.valueOf(mealType.toUpperCase()));
		}
		
		if (difficultyLevel != null) {
		recipeFiltro.setDifficultyLevel(DifficultyLevel.valueOf(difficultyLevel.toUpperCase()));
		}
			
		List<Recipe> recipes = recipeService.search(recipeFiltro);
		return ResponseEntity.ok(recipes);
	}
	
	private Recipe convert (RecipeDTO dto) {
		Recipe recipe = new Recipe();
		recipe.setId(dto.getId());
		recipe.setTitle(dto.getTitle());
		recipe.setServedPeople(dto.getServedPeople());
		recipe.setIngredients(dto.getIngredients());
		recipe.setInstructions(dto.getInstructions());
		
		if (dto.getMealType() != null) {
			recipe.setMealType(MealType.valueOf(dto.getMealType()));
		} 		
		
		if (dto.getServedPeople() != null) {
			recipe.setDifficultyLevel(DifficultyLevel.valueOf(dto.getDifficultyLevel()));
		}
		return recipe;
	}
	
	
}
