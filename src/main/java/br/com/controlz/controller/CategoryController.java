package br.com.controlz.controller;

import br.com.controlz.domain.dto.CategoryDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.exception.CategoryDeleteException;
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
	public ResponseEntityCustom registerNewCategory(@RequestBody CategoryDTO categoryDTO) throws CategoryNotFoundException {
		return categoryService.registerNewCategory(categoryDTO);
	}

	@GetMapping(value = "/all/{registerId}")
	@ApiOperation(value = "Método que retorna todas categorias por registerId")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public List<CategoryDTO> getAllCategoriesByRegisterId(@PathVariable Long registerId) throws CategoryNotFoundException {
		return categoryService.getAllCategoriesByRegisterId(registerId);
	}

	@GetMapping(value = "/{categoryId}")
	@ApiOperation(value = "Método que retorna a categoria pelo id")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public CategoryDTO getCategoryId(@PathVariable Long categoryId) throws CategoryNotFoundException {
		return categoryService.getCategoryId(categoryId);
	}

	@PutMapping(value = "/update")
	@ApiOperation(value = "Método que atualiza a categoria")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntityCustom updateCategoryById(@RequestBody CategoryDTO categoryDTO) throws CategoryNotFoundException {
		return categoryService.updateCategory(categoryDTO);
	}

	@DeleteMapping(value = "/{categoryId}/{registerId}")
	@ApiOperation(value = "Método que deleta uma categoria por Id")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntityCustom deleteCategoryById(@PathVariable Long categoryId, @PathVariable Long registerId) throws CategoryNotFoundException, CategoryDeleteException {
		return categoryService.deleteCategoryById(categoryId, registerId);
	}

}
