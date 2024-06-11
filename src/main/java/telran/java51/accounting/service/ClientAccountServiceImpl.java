package telran.java51.accounting.service;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.accounting.dao.UserAccountRepository;
import telran.java51.accounting.dto.ClientDto;
import telran.java51.accounting.dto.ClientRegisterDto;
import telran.java51.accounting.dto.ClientRolesDto;
import telran.java51.accounting.dto.ClientUpdateDto;
import telran.java51.accounting.dto.exceptions.UserExistException;
import telran.java51.accounting.model.ClientAccount;
import telran.java51.accounting.model.ClientRole;

@Service
@RequiredArgsConstructor
public class ClientAccountServiceImpl implements ClientAccountService, CommandLineRunner {
	
	final UserAccountRepository userAccountRepository;
	final ModelMapper modelMapper;
	final PasswordEncoder passwordEncoder;

	@Override
	public ClientDto register(ClientRegisterDto clientRegisterDto) {
		
		if(userAccountRepository.existsById(clientRegisterDto.getLogin())) {
			throw new UserExistException();
		}
		ClientAccount clientAccount = modelMapper.map(clientRegisterDto, ClientAccount.class);
		String password = passwordEncoder.encode(clientRegisterDto.getPassword());
		clientAccount.setPassword(password);
		userAccountRepository.save(clientAccount);
		return modelMapper.map(clientAccount, ClientDto.class);
	}

	@Override
	public ClientDto getUser(String login) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDto removeUser(String login) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDto updateUser(String login, ClientUpdateDto clientUpdateDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientRolesDto addRole(String login, String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientRolesDto removeRole(String login, String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changePassword(String login, String newPassword) {
		// TODO Auto-generated method stub

	}

	@Override
	public void recoveryPasswordLink(String email) {
		// TODO Auto-generated method stub

	}

	@Override
	public void recoveryPassword(String token, String password) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void run(String... args) throws Exception {      //creating admin admin
		if (!userAccountRepository.existsById("user1")) {
			String password = passwordEncoder.encode("user1");
			ClientAccount clientAccount = new ClientAccount("user1", password, "John", "Smith");
			userAccountRepository.save(clientAccount);
		}

	}

}
