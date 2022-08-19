package com.crud.practice.tests;

import java.time.Instant;

import com.crud.practice.entities.Person;

public class Factory {
	
	public static Person createPerson() {
		Person person = new Person();
		person.setName("João");
		person.setPhoneNumber("75 98129-0360");
		person.setCpf("086.395.593-32");
		person.setBirthDate(Instant.parse("1999-08-01T10:09:20Z"));
		return person;
	}
	
}
