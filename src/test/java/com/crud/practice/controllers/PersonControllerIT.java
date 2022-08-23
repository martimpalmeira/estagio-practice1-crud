package com.crud.practice.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.crud.practice.dto.PersonDTO;
import com.crud.practice.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	private Long existingId;

	private Long notExistingId;

	private Long countTotalPerson;

	private PersonDTO personDTO;

	@BeforeEach
	void setUo() throws Exception {
		existingId = 1L;
		notExistingId = 1000L;
		countTotalPerson = 6L;
		personDTO = Factory.creatPersonDTO();
	}
	
	@Test
	void findAllPaged_ShouldReturnPersonDTOPageSortedByName() throws Exception{
		ResultActions result = mockMvc
		.perform(get("/persons?sort=name,asc")
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalPerson));
		result.andExpect(jsonPath("$.content[0].name").value("Beatriz Barbosa"));
		result.andExpect(jsonPath("$.content[1].name").value("Felipe Santos"));
		result.andExpect(jsonPath("$.content[2].name").value("João Carlos"));
			
	}
	
	@Test
	void findById_ShouldReturnPersonDTO_WhenIdExists()  throws Exception{
		ResultActions result = mockMvc
				.perform(get("/persons/{id}", this.existingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.cpf").exists());
		result.andExpect(jsonPath("$.phoneNumber").exists());
		result.andExpect(jsonPath("$.birthDate").exists());
	
	}
	
	@Test
	void update_ShouldUpdateAndReturnNewPerson_WhenIdExists()  throws Exception{
		String jsonBody = objectMapper.writeValueAsString(personDTO);
		
		ResultActions result = mockMvc
				.perform(put("/persons{id}", this.existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].name").value(personDTO.getName()));
		result.andExpect(jsonPath("$.content[0].cpf").value(personDTO.getCpf()));
		result.andExpect(jsonPath("$.content[0].phoneNumber").value(personDTO.getPhoneNumber()));

	}
	
	@Test
	void update_ShouldUpdateAndReturnNotFound_WhenIdDoesNotExist() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(personDTO);
		
		ResultActions result = mockMvc
				.perform(put("/persons{id}", this.notExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.name").doesNotExist());
		result.andExpect(jsonPath("$.cpf").doesNotExist());
		result.andExpect(jsonPath("$.phoneNumber").doesNotExist());

	}

}
