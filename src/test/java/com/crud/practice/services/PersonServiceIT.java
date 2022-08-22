package com.crud.practice.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.crud.practice.dto.PersonDTO;
import com.crud.practice.repositories.PersonRepository;
import com.crud.practice.services.exceptions.ResourceNotFoundException;
import com.crud.practice.tests.Factory;

@SpringBootTest
@Transactional
class PersonServiceIT {

	@Autowired
	private PersonService service;

	@Autowired
	private PersonRepository repository;

	private Long existingId;

	private Long notExistingId;

	private Long countTotalPerson;

	private PersonDTO personDTO;

	private Pageable pageable;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		notExistingId = 1000L;
		countTotalPerson = 6L;
		personDTO = Factory.creatPersonDTO();
	}

	@Test
	void delete_ShouldDeletePerson_WhenIdExists() {
		service.delete(existingId);
		Assertions.assertEquals(countTotalPerson - 1L, repository.count());
	}

	@Test
	void delete_ShouldThrowResourceNotFound_WhenIdDoesNotExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(notExistingId);
		});
	}

	@Test
	void findAllPaged_ShouldReturnPage0With10PersonsDTO() {
		pageable = PageRequest.of(0, 10);

		Page<PersonDTO> pageDTO = service.findAllPaged(pageable);

		Assertions.assertFalse(pageDTO.isEmpty());
		Assertions.assertEquals(0, pageDTO.getNumber());
		Assertions.assertEquals(10, pageDTO.getSize());
		Assertions.assertEquals(this.countTotalPerson, pageDTO.getTotalElements());
	}

	@Test
	void findAllPaged_ShouldReturnEmptyPage_WhenPageDoesNotExist() {
		pageable = PageRequest.of(50, 10);

		Page<PersonDTO> pageDTO = service.findAllPaged(pageable);

		Assertions.assertTrue(pageDTO.isEmpty());
	}

	@Test
	void findAllPaged_ShouldReturnPage0With10PersonsSortedByName() {
		pageable = PageRequest.of(0, 10, Sort.by("name"));

		Page<PersonDTO> pageDTO = service.findAllPaged(pageable);

		Assertions.assertEquals("Beatriz Barbosa", pageDTO.getContent().get(0).getName());
		Assertions.assertEquals("Felipe Santos", pageDTO.getContent().get(1).getName());
		Assertions.assertEquals("João Carlos", pageDTO.getContent().get(2).getName());
	}

	@Test
	void insert_ShoudSavePersonInDataBase() {

		personDTO = service.insert(personDTO);

		Assertions.assertTrue(repository.count() == this.countTotalPerson + 1);
		Assertions.assertTrue(personDTO.getId() == this.countTotalPerson + 1);
		Assertions.assertNotNull(personDTO);
	}

	@Test
	void update_ShouldUpdatePerson_WhenIdExists() {

		PersonDTO dto = service.update(personDTO, existingId);

		Assertions.assertTrue(dto.getId() == personDTO.getId());
		Assertions.assertNotNull(dto);
	}

	@Test
	void update_ShouldThrowResourceNotFound_WhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(personDTO, notExistingId);
		});
	}

}
