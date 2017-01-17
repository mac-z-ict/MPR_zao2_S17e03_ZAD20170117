package dao;

import java.util.List;

import domain.Person;
import domain.User;

public interface IPersonRepository extends IRepository<Person> {

	public List<Person> withName(String name);
	public Person byUser(User user);
}
