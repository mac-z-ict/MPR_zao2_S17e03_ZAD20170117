package domain.builders;

import domain.Address;
import domain.Person;

public class AddressBuilder {

	private Address address;
	
	public AddressBuilder() {
		address = new Address();
	}
	
	public AddressBuilder withId(int id) {
		address.setId(id);
		return this;
	}
	
	public AddressBuilder withStreetName(String streetName) {
		address.setStreetName(streetName);
		return this;
	}
	
	public AddressBuilder withStreetNumber(int streetNumber) {
		address.setStreetNumber(streetNumber);
		return this;
	}
	
	public AddressBuilder withHouseNumber(String houseNumber) {
		address.setHouseNumber(houseNumber);
		return this;
	}
	
	public AddressBuilder withCity(String city) {
		address.setCity(city);
		return this;
	}
	
	public AddressBuilder withPostcode(String postcode) {
		address.setPostcode(postcode);
		return this;
	}
	
	public AddressBuilder withPerson(Person person) {
		address.setPerson(person);
		return this;
	}
	
	public Address build() {
		return address;
	}
}
