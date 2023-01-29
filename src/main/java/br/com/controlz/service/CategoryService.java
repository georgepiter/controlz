package br.com.controlz.service;

import br.com.controlz.domain.dto.CategoryDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.entity.Category;
import br.com.controlz.domain.exception.CategoryNotFoundException;
import br.com.controlz.domain.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public ResponseEntityCustom registerNewCategory(CategoryDTO categoryDTO) {
		Category category = new Category(categoryDTO.getDescription());
		categoryRepository.save(category);
		return new ResponseEntityCustom(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Nova categoria salva com sucesso!");
	}

	public List<CategoryDTO> getAllCategories() throws CategoryNotFoundException {
		List<Category> categories = categoryRepository.findAll();
		if (categories.isEmpty()) {
			throw new CategoryNotFoundException("Nenhuma categoria localizada na base");
		}
		List<CategoryDTO> categoryDTOS = new ArrayList<>();
		categories.forEach(category -> {
			CategoryDTO newCategory = new CategoryDTO();
			newCategory.setCategoryId(category.getCategoryId());
			newCategory.setDescription(category.getDescription());
			categoryDTOS.add(newCategory);
		});
		return categoryDTOS;
	}

	public ResponseEntityCustom deleteCategoryById(Long categoryId) throws CategoryNotFoundException {
		Optional<Category> category = categoryRepository.findById(categoryId);
		if (category.isEmpty()) {
			throw new CategoryNotFoundException("Categoria não encontrada pelo ID");
		}
		categoryRepository.delete(category.get());
		return new ResponseEntityCustom(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, "Categoria deletada com sucesso!");
	}

	public ResponseEntityCustom updateCategory(CategoryDTO categoryDTO) throws CategoryNotFoundException {
		Optional<Category> category = categoryRepository.findById(categoryDTO.getCategoryId());
		if (category.isEmpty()) {
			throw new CategoryNotFoundException("Categoria não encontrada pelo ID");
		}
		Category updateCategory = new Category();
		updateCategory.setCategoryId(categoryDTO.getCategoryId());
		updateCategory.setDescription(categoryDTO.getDescription());
		categoryRepository.save(updateCategory);
		return new ResponseEntityCustom(HttpStatus.OK.value(), HttpStatus.OK, "Categoria atualizada com sucesso!");
	}

	public CategoryDTO getCategoryId(Long categoryId) throws CategoryNotFoundException {
		Optional<Category> category = categoryRepository.findById(categoryId);
		if (category.isEmpty()) {
			throw new CategoryNotFoundException("Categoria não encontrada pelo Id");
		}
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryId(category.get().getCategoryId());
		categoryDTO.setDescription(category.get().getDescription());
		return categoryDTO;
	}
}
