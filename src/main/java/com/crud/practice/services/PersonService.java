package com.crud.practice.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crud.practice.dto.PersonDTO;
import com.crud.practice.entities.Person;
import com.crud.practice.repositories.PersonRepository;

@Service
public class PersonService {
	
	@Autowired
	private PersonRepository repository;
	
	@Transactional(readOnly = true)
	public Page<PersonDTO> findAllPaged (Pageable pageable) {
		Page<Person> page = repository.findAll(pageable);
		return page.map(person -> new PersonDTO(person));		
	}
	
	@Transactional(readOnly = true)
	public PersonDTO findById (Long id) {
		Optional<Person> obj = repository.findById(id);
		return new PersonDTO(obj.get());
	}
	
	@Transactional
	public PersonDTO insert (PersonDTO dto) {
		Person entity = new Person();
		copyDTOtoEntity(entity, dto);
		entity = repository.save(entity);
		return new PersonDTO(entity);
	}
	
	@Transactional
	public PersonDTO update (PersonDTO dto, Long id) {
		Person entity = repository.getById(id);
		copyDTOtoEntity(entity, dto);
		entity = repository.save(entity);
		return new PersonDTO(entity);	
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	private void copyDTOtoEntity(Person entity, PersonDTO dto) {
		entity.setName(dto.getName());
		entity.setBirthDate(dto.getBirthDate());
		entity.setAdress(dto.getAdress());
		entity.setCpf(dto.getCpf());
		entity.setPhoneNumber(dto.getPhoneNumber());
	}
	

	
	
}
