package com.crud.practice.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.crud.practice.dto.PersonDTO;
import com.crud.practice.services.PersonService;

@RestController
@RequestMapping(value = "/persons")
public class PersonController {
	
	@Autowired
	private PersonService service;
	
	@GetMapping
	public ResponseEntity<Page<PersonDTO>> findAllPaged(Pageable pageable){
		Page<PersonDTO> list = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PersonDTO> findById(@PathVariable Long id){
		PersonDTO personDTO = service.findById(id);
		return ResponseEntity.ok().body(personDTO);
	}
	
	@PostMapping
	public ResponseEntity<PersonDTO> insert(@RequestBody PersonDTO dto){
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PersonDTO> update(@RequestBody PersonDTO dto, @PathVariable Long id){
		dto = service.update(dto, id);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	
	
}
