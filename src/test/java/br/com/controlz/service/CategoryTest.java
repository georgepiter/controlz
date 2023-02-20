package br.com.controlz.service;

import br.com.controlz.domain.dto.CategoryDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.entity.Category;
import br.com.controlz.domain.exception.CategoryNotFoundException;
import br.com.controlz.domain.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryTest {

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private CategoryService categoryService;

	@BeforeEach
	public void setUp() {
		categoryService = new CategoryService(this.categoryRepository);
	}

	@Test
	@DisplayName("Deve criar nova categoria e retornar status 201")
	void shouldCreateNewCategoryAndReturn201() throws CategoryNotFoundException {
		// given
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setDescription("Nova categoria");

		// when
		ResponseEntityCustom response = categoryService.registerNewCategory(categoryDTO);

		// then
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		assertEquals(HttpStatus.CREATED, response.getError());
		assertEquals("Nova categoria salva com sucesso!", response.getMessage());

		ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
		verify(categoryRepository).save(categoryCaptor.capture());
		assertEquals(categoryDTO.getDescription(), categoryCaptor.getValue().getDescription());
	}

	@Test
	@DisplayName("Deve retornar erro 400 quando a descrição da categoria for nula")
	void shouldReturnError400WhenCategoryDescriptionIsNull() {
		// given
		CategoryDTO categoryDTO = new CategoryDTO();

		// when
		assertThrows(CategoryNotFoundException.class, () -> categoryService.registerNewCategory(categoryDTO));

		// then
		verify(categoryRepository, never()).save(any());
	}

	@Test
	@DisplayName("Deve retornar uma lista de categorias não vazia")
	void shouldReturnNonEmptyListOfCategories() throws CategoryNotFoundException {
		// given
		List<Category> categories = new ArrayList<>();

		categories.add(new Category("Categoria 1"));
		categories.add(new Category("Categoria 2"));
		categoryRepository.saveAll(categories);
		when(categoryRepository.findAll()).thenReturn(categories);

		// when
		List<CategoryDTO> categoryDTOs = categoryService.getAllCategories();

		// then
		assertFalse(categoryDTOs.isEmpty());
		assertEquals(2, categoryDTOs.size());
		assertEquals("Categoria 1", categoryDTOs.get(0).getDescription());
		assertEquals("Categoria 2", categoryDTOs.get(1).getDescription());
	}

	@Test
	@DisplayName("Deve retornar uma lista de categorias vazia")
	void shouldReturnEmptyListOfCategories() {
		// given
		when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

		// when/then
		assertThrows(CategoryNotFoundException.class, () -> categoryService.getAllCategories());
	}

	@Test
	@DisplayName("Deve retornar CategoryDTO ao buscar categoria pelo ID")
	void shouldReturnCategoryDTOWhenFindCategoryById() throws CategoryNotFoundException {
		// given
		Long categoryId = 1L;
		Category category = new Category("Categoria 1");
		category.setCategoryId(categoryId);
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

		// when
		CategoryDTO result = categoryService.getCategoryId(categoryId);

		// then
		assertEquals(categoryId, result.getCategoryId());
		assertEquals(category.getDescription(), result.getDescription());
	}

	@Test
	@DisplayName("Deve lançar CategoryNotFoundException quando não encontrar categoria pelo ID")
	void shouldThrowCategoryNotFoundExceptionWhenFindCategoryById() {
		// given
		Long categoryId = 1L;
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

		// when/then
		assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryId(categoryId));
	}

	@Test
	@DisplayName("Deve atualizar uma categoria existente")
	void shouldUpdateExistingCategory() throws CategoryNotFoundException {
		// given
		Long categoryId = 1L;
		String newDescription = "Nova descrição da categoria";
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryId(categoryId);
		categoryDTO.setDescription(newDescription);
		Category existingCategory = new Category("Descrição atual da categoria");
		existingCategory.setCategoryId(categoryId);
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

		// when
		ResponseEntityCustom response = categoryService.updateCategory(categoryDTO);

		// then
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(HttpStatus.OK, response.getError());
		assertEquals("Categoria atualizada com sucesso!", response.getMessage());

		ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
		verify(categoryRepository).save(categoryCaptor.capture());
		Category updatedCategory = categoryCaptor.getValue();
		assertEquals(categoryId, updatedCategory.getCategoryId());
		assertEquals(newDescription, updatedCategory.getDescription());
	}

	@Test
	@DisplayName("Deve excluir categoria existente e retornar status 204")
	void shouldDeleteExistingCategoryAndReturn204() throws CategoryNotFoundException {
		// given
		Long categoryId = 1L;
		Category category = new Category("Nova categoria");
		category.setCategoryId(categoryId);

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

		// when
		ResponseEntityCustom response = categoryService.deleteCategoryById(categoryId);

		// then
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
		assertEquals(HttpStatus.NO_CONTENT, response.getError());
		assertEquals("Categoria deletada com sucesso!", response.getMessage());

		verify(categoryRepository).delete(category);
	}

	@Test
	@DisplayName("Deve lançar CategoryNotFoundException ao tentar excluir categoria inexistente")
	void shouldThrowCategoryNotFoundExceptionWhenDeletingNonexistentCategory() {
		// given
		Long categoryId = 1L;

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

		// when/then
		assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategoryById(categoryId));

		verify(categoryRepository, never()).delete(any());
	}

}
