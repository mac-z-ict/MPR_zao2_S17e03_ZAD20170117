package domain.builders;

import java.util.Set;

import domain.Address;
import domain.Person;
import domain.User;

public class PersonBuilder {
	private Person person;
	
	public PersonBuilder() {
		person = new Person();
	}
	
	public PersonBuilder withId(int id) {
		person.setId(id);
		return this;
	}
	
	public PersonBuilder withName(String name) {
		person.setName(name);
		return this;
	}
	
	public PersonBuilder withSurname(String surname) {
		person.setSurname(surname);
		return this;
	}
	
	public PersonBuilder withAge(int age) {
		person.setAge(age);
		return this;
	}
	
	public PersonBuilder withUser(User user) {
		person.setUser(user);
		return this;
	}
	
	public PersonBuilder withAddresses(Set<Address> addresses) {
		person.setAddresses(addresses);
		return this;
	}
	
	public Person build() {
		return person;
	}
}
