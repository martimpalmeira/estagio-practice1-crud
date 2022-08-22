package com.crud.practice.dto;

import java.io.Serializable;
import java.time.Instant;

import com.crud.practice.entities.Person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private String cpf;
	
	private String phoneNumber;
	
	private Instant birthDate;
	
	private String adress;
	
	public PersonDTO(Person person) {
		this.name = person.getName();
		this.adress = person.getAdress();
		this.birthDate = person.getBirthDate();
		this.cpf = person.getCpf();
		this.id = person.getId();
		this.phoneNumber = person.getPhoneNumber();
	
	}
	
	
	
	
	
	
}
