package com.crud.practice.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.crud.practice.dto.PersonDTO;
import com.crud.practice.services.PersonService;
import com.crud.practice.services.exceptions.ResourceNotFoundException;
import com.crud.practice.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PersonController.class)
class PersonControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PersonService service;

	@Autowired
	private ObjectMapper objectMapper;

	private Long existingId;

	private Long notExistingId;

	private PersonDTO personDTO;

	private PageImpl<PersonDTO> page;

	@BeforeEach
	void setUp() throws Exception {

		existingId = 1L;

		notExistingId = 1000l;

		personDTO = Factory.creatPersonDTO();

		page = new PageImpl<PersonDTO>(List.of(personDTO));

		when(service.findAllPaged(any())).thenReturn(page);

		when(service.findById(existingId)).thenReturn(personDTO);
		when(service.findById(notExistingId)).thenThrow(ResourceNotFoundException.class);

		when(service.insert(any())).thenReturn(personDTO);

		when(service.update(any(), eq(existingId))).thenReturn(personDTO);
		when(service.update(any(), eq(notExistingId))).thenThrow(ResourceNotFoundException.class);

		Mockito.doNothing().when(service).delete(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(notExistingId);

	}

	@Test
	void findAllPaged_ShouldReturnPersonDTOPage() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/persons")
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
	}
	
	@Test
	void findById_ShouldReturnPersonDTO_WhenIdExists() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/persons/{id}",this.existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.cpf").exists());
		result.andExpect(jsonPath("$.adress").exists());
		result.andExpect(jsonPath("$.birthDate").exists());
	}
	
	@Test
	void findById_ShoudReturnNotFound_WhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc
		.perform(get("/persons/{id}", this.notExistingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.name").doesNotExist());
		result.andExpect(jsonPath("$.cpf").doesNotExist());
		result.andExpect(jsonPath("$.adress").doesNotExist());
		result.andExpect(jsonPath("$.birthDate").doesNotExist());
	}
	
	@Test
	void insert_ShouldReturnCreatedStatus() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(personDTO);
		
		ResultActions result = mockMvc
		.perform(post("/persons")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.cpf").exists());
		result.andExpect(jsonPath("$.phoneNumber").exists());
		result.andExpect(jsonPath("$.adress").exists());
		result.andExpect(jsonPath("$.birthDate").exists());
	}
	
	@Test
	void update_ShouldReturnPersonDTO_WhenIdExists() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(personDTO);
		
		ResultActions result = mockMvc.perform(put("/persons/{id}", this.existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.cpf").exists());
		result.andExpect(jsonPath("$.adress").exists());
		result.andExpect(jsonPath("$.phoneNumber").exists());
	}
	
	@Test
	void update_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(personDTO);
		
		ResultActions result = mockMvc.perform(put("/persons/{id}", this.notExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.name").doesNotExist());
		result.andExpect(jsonPath("$.cpf").doesNotExist());
		result.andExpect(jsonPath("$.adress").doesNotExist());
		result.andExpect(jsonPath("$.phoneNumber").doesNotExist());
	}
	
	@Test
	void delete_ShouldDoNothin_WhenIdExists() throws Exception {
		ResultActions result = mockMvc
				.perform(delete("/persons/{id}", this.existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
		
		result.andExpect(jsonPath("$.id").doesNotExist());
		result.andExpect(jsonPath("$.cpf").doesNotExist());
		result.andExpect(jsonPath("$.adress").doesNotExist());
		result.andExpect(jsonPath("$.phoneNumber").doesNotExist());	
	}
	
	@Test
	void delete_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc
				.perform(delete("/persons/{id}", this.notExistingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		
		result.andExpect(jsonPath("$.id").doesNotExist());
		result.andExpect(jsonPath("$.cpf").doesNotExist());
		result.andExpect(jsonPath("$.adress").doesNotExist());
		result.andExpect(jsonPath("$.phoneNumber").doesNotExist());	
	}
}
