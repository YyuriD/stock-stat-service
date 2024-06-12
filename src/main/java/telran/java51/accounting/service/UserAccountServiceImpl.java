package telran.java51.accounting.service;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.accounting.dao.UserAccountRepository;
import telran.java51.accounting.dto.UserDto;
import telran.java51.accounting.dto.UserRegisterDto;
import telran.java51.accounting.dto.UserRolesDto;
import telran.java51.accounting.dto.UserUpdateDto;
import telran.java51.accounting.dto.exceptions.UserExistException;
import telran.java51.accounting.dto.exceptions.UserNotFoundException;
import telran.java51.accounting.model.Role;
import telran.java51.accounting.model.UserAccount;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner {
	
	final UserAccountRepository userAccountRepository;
	final ModelMapper modelMapper;
	final PasswordEncoder passwordEncoder;

	@Override
	public UserDto register(UserRegisterDto userRegisterDto) {
		
		if(userAccountRepository.existsById(userRegisterDto.getLogin())) {
			throw new UserExistException();
		}
		UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
		String password = passwordEncoder.encode(userRegisterDto.getPassword());
		userAccount.setPassword(password);
		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto getUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto removeUser(String login) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDto updateUser(String login, UserUpdateDto clientUpdateDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRolesDto addRole(String login, String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRolesDto removeRole(String login, String role) {
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
		if (!userAccountRepository.existsById("admin")) {
			String password = passwordEncoder.encode("admin");
			UserAccount userAccount = new UserAccount("admin", password, "", "");
			userAccount.addRole(Role.ADMIN.name());
			userAccountRepository.save(userAccount);
		}

	}

}
