package dao;

import domain.User;

public interface IUserRepository extends IRepository<User> {
	public User withLogin(String login);
	public User withLoginAndPassword(String login, String password);
}
