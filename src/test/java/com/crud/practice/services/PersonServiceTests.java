package com.crud.practice.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.crud.practice.dto.PersonDTO;
import com.crud.practice.entities.Person;
import com.crud.practice.repositories.PersonRepository;
import com.crud.practice.services.exceptions.ResourceNotFoundException;
import com.crud.practice.tests.Factory;

@ExtendWith(SpringExtension.class)
class PersonServiceTests {

	@InjectMocks
	private PersonService service;

	@Mock
	private PersonRepository repository;

	private Long existingId;

	private Long notExistingId;

	private PageImpl<Person> page;

	private Person person;

	private PersonDTO personDTO;
	
	private Person personNullId;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		notExistingId = 1000L;
		person = Factory.createPerson();
		page = new PageImpl<>(List.of(person));
		personDTO = Factory.creatPersonDTO();
		personNullId = Factory.createPersonNullId();

		doNothing().when(repository).deleteById(existingId);
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(notExistingId);

		when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		when(repository.findById(existingId)).thenReturn(Optional.of(person));
		when(repository.findById(notExistingId)).thenReturn(Optional.empty());

		when(repository.save(ArgumentMatchers.any())).thenReturn(person);

		when(repository.getById(existingId)).thenReturn(person);
		when(repository.getById(notExistingId)).thenThrow(EntityNotFoundException.class);

	}

	@Test
	void delete_ShouldDoNothing_WhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});

		verify(repository).deleteById(existingId);
	}
	
	@Test
	void delete_ShouldThrowResourceNotFound_WhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(notExistingId);
		});
		
		verify(repository).deleteById(notExistingId);
	}
	
	@Test
	void findAllPaged_ShouldReturnPageOfPersonDTO() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<PersonDTO> pageDTO = service.findAllPaged(pageable);
		
		Assertions.assertTrue(!pageDTO.isEmpty());
		
		verify(repository).findAll(pageable);
	}
	
	@Test
	void findById_ShouldReturnPersonDTO_WhenIdExists() {
		PersonDTO dto = service.findById(existingId);
		
		Assertions.assertEquals(existingId, dto.getId());
		
		Assertions.assertNotNull(dto);
		
		verify(repository).findById(existingId);		
	}
	
	@Test
	void findById_ShouldThrowResourceNotFoundException_WhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(notExistingId);
		});
		
		verify(repository).findById(notExistingId);
	}
	
	@Test
	void insert_ShouldReturnPersonDTO_WhenIdExists() {
		personDTO = service.insert(personDTO);
		
		Assertions.assertNotNull(personDTO);
		Assertions.assertEquals(personDTO.getId(), existingId);
	
		verify(repository).save(personNullId);
	}
	
	@Test
	void update_ShouldReturnPersonDTO_WhenIdExists() {
		personDTO = service.update(personDTO, existingId);
		
		Assertions.assertNotNull(personDTO);
		
		verify(repository).getById(existingId);
		verify(repository).save(person);
	}
	
	
	
	
	
	
}
	
