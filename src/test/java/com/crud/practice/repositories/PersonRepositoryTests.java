package com.crud.practice.repositories;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.crud.practice.entities.Person;
import com.crud.practice.tests.Factory;

@DataJpaTest
class PersonRepositoryTests {

	@Autowired
	private PersonRepository repository;

	private Long existingId;

	private Long nonExistingId;

	private Long countTotalPersons;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalPersons = 6L;
	}

	@Test
	public void findAll_ShouldReturnAllPersons() {
		List<Person> list = repository.findAll();

		Assertions.assertEquals(countTotalPersons, list.size());
	}

	@Test
	public void findById_ShouldReturnOptionalPerson_whenIdExists() {
		Optional<Person> obj = repository.findById(existingId);

		Assertions.assertNotNull(obj);
	}

	@Test
	public void findById_ShouldReturnEmptyOptional_whenIdDoesNotExists() {
		Optional<Person> obj = repository.findById(nonExistingId);

		Assertions.assertTrue(obj.isEmpty());
	}

	@Test
	public void save_ShouldPersistPersonWithAutoIncrement_WhenIdIsNull() {
		Person person = Factory.createPerson();

		person = repository.save(person);

		Assertions.assertEquals(person.getId(), countTotalPersons + 1);
	}

	@Test
	public void deleteById_ShouldDeletePerson_WhenIdExists() {
		repository.deleteById(existingId);

		Optional<Person> person = repository.findById(existingId);

		Assertions.assertTrue(person.isEmpty());
	}

	@Test
	public void deleteById_ShouldThrowEmptyResultDataAccessException_WhenIdDoesNotExists() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, ()->{
			repository.deleteById(nonExistingId);
		});
		
	}

}
