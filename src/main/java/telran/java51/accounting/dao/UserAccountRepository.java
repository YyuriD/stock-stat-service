package telran.java51.accounting.dao;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import telran.java51.accounting.model.ClientAccount;
import telran.java51.accounting.model.UserAccount;

public interface UserAccountRepository extends CrudRepository<UserAccount, String> {
	Optional<ClientAccount> findClientByLogin(String login);	
	Stream<ClientAccount> findAllAdminsBy();
}
