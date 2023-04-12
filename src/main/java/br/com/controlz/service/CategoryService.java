package br.com.controlz.service;

import br.com.controlz.domain.dto.CategoryDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.entity.Category;
import br.com.controlz.domain.exception.CategoryDeleteException;
import br.com.controlz.domain.exception.CategoryNotFoundException;
import br.com.controlz.domain.repository.CategoryRepository;
import br.com.controlz.domain.repository.DebtRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final DebtRepository debtRepository;

	public CategoryService(CategoryRepository categoryRepository,
	                       DebtRepository debtRepository) {
		this.categoryRepository = categoryRepository;
		this.debtRepository = debtRepository;
	}

	public ResponseEntityCustom registerNewCategory(CategoryDTO categoryDTO) throws CategoryNotFoundException {
		if (Objects.isNull(categoryDTO.getDescription())) {
			throw new CategoryNotFoundException("Necessário a descrição da categoria");
		}
		Category category = new Category(categoryDTO.getDescription(), categoryDTO.getRegisterId());
		categoryRepository.save(category);
		return new ResponseEntityCustom(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Nova categoria salva com sucesso!");
	}

	public List<CategoryDTO> getAllCategories() throws CategoryNotFoundException {
		List<Category> categories = categoryRepository.findAll();
		return validateAndBuildCategoryListDTO(categories);
	}

	private static List<CategoryDTO> validateAndBuildCategoryListDTO(List<Category> categories) throws CategoryNotFoundException {
		if (categories.isEmpty()) {
			throw new CategoryNotFoundException("Nenhuma categoria encontrada na base");
		}
		List<CategoryDTO> categoryDTOs = new ArrayList<>();

		categories.forEach(category -> {
			CategoryDTO categoryDTO = new CategoryDTO();
			categoryDTO.setCategoryId(category.getCategoryId());
			categoryDTO.setDescription(category.getDescription());
			categoryDTO.setRegisterId(category.getRegisterId());
			categoryDTOs.add(categoryDTO);
		});

		return categoryDTOs;
	}

	private static CategoryDTO buildCategoryDTO(Category category) {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryId(category.getCategoryId());
		categoryDTO.setDescription(category.getDescription());
		categoryDTO.setRegisterId(category.getCategoryId());
		return categoryDTO;
	}

	public ResponseEntityCustom deleteCategoryById(Long categoryId, Long registerId) throws CategoryNotFoundException, CategoryDeleteException {
		Category category = categoryRepository.findByCategoryIdAndRegisterId(categoryId, registerId)
				.orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada pelo ID"));

		debtRepository.updateCategoryIdOnDebt(category.getCategoryId());
		try {
			categoryRepository.delete(category);
		} catch (DataIntegrityViolationException e) {
			throw new CategoryDeleteException();
		}
		return new ResponseEntityCustom(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, "Categoria deletada com sucesso!");
	}

	public ResponseEntityCustom updateCategory(CategoryDTO categoryDTO) throws CategoryNotFoundException {
		Category category = categoryRepository.findById(categoryDTO.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada pelo ID"));
		category.setCategoryId(categoryDTO.getCategoryId());
		category.setDescription(categoryDTO.getDescription());
		category.setRegisterId(categoryDTO.getRegisterId());
		categoryRepository.save(category);
		return new ResponseEntityCustom(HttpStatus.OK.value(), HttpStatus.OK, "Categoria atualizada com sucesso!");
	}

	public CategoryDTO getCategoryId(Long categoryId) throws CategoryNotFoundException {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada pelo Id"));
		return buildCategoryDTO(category);
	}

	public List<CategoryDTO> getAllCategoriesByRegisterId(Long registerId) throws CategoryNotFoundException {
		List<Category> categories = categoryRepository.findByRegisterId(registerId);
		return validateAndBuildCategoryListDTO(categories);
	}
}
