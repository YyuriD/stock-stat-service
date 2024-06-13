package telran.java51.accounting.service;

import telran.java51.accounting.dto.UserDto;
import telran.java51.accounting.dto.UserRegisterDto;
import telran.java51.accounting.dto.UserRolesDto;
import telran.java51.accounting.dto.UserUpdateDto;

public interface UserAccountService {
	
	UserDto register(UserRegisterDto userRegisterDto);
	
	UserDto getUser(String login);
	
	UserDto removeUser(String login);
	
	UserDto updateUser(String login, UserUpdateDto userUpdateDto);
	
	UserRolesDto addRole(String login, String role);
	
	UserRolesDto removeRole(String login, String role);
	
	void changePassword(String login, String newPassword);

	void recoveryPasswordLink(String email);

	void recoveryPassword(String token, String password);
	
}
