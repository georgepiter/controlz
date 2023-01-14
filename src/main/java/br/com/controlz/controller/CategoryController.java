package br.com.controlz.controller;

import br.com.controlz.domain.dto.CategoryDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.exception.CategoryNotFoundException;
import br.com.controlz.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "Categoria", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Categoria"})
@RequestMapping(value = "api/v1/category")
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@PostMapping(value = "/")
	@ApiOperation(value = "Método que registra uma nova categoria")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntityCustom registerNewCategory(@RequestBody CategoryDTO categoryDTO) {
		return categoryService.registerNewCategory(categoryDTO);
	}

	@GetMapping(value = "/all")
	@ApiOperation(value = "Método que retorna todas categorias")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public List<CategoryDTO> getAllCategories() throws CategoryNotFoundException {
		return categoryService.getAllCategories();
	}

	@DeleteMapping(value = "/idCategory/{idCategory}")
	@ApiOperation(value = "Método que deleta um débito por ID debt")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntityCustom deleteCategoryById(@PathVariable Long idCategory) throws CategoryNotFoundException {
		return categoryService.deleteCategoryById(idCategory);
	}

}
