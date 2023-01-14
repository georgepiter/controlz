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
		return new ResponseEntityCustom(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Category created successfully");
	}

	public List<CategoryDTO> getAllCategories() throws CategoryNotFoundException {
		List<Category> categories = categoryRepository.findAll();
		if (categories.isEmpty()) {
			throw new CategoryNotFoundException("no category found");
		}
		List<CategoryDTO> categoryDTOS = new ArrayList<>();
		categories.forEach(
				category -> {
					CategoryDTO newCategory = new CategoryDTO();
					newCategory.setIdCategory(category.getIdCategory());
					newCategory.setDescription(category.getDescription());
					categoryDTOS.add(newCategory);
				}
		);
		return categoryDTOS;
	}

	public ResponseEntityCustom deleteCategoryById(Long idCategory) throws CategoryNotFoundException {
		Optional<Category> category = categoryRepository.findById(idCategory);
		if (category.isEmpty()) {
			throw new CategoryNotFoundException("category not found by id");
		}
		categoryRepository.delete(category.get());
		return new ResponseEntityCustom(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, "category deleted");
	}
}
