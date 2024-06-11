package telran.java51.accounting.service;

import telran.java51.accounting.dto.ClientDto;
import telran.java51.accounting.dto.ClientRegisterDto;
import telran.java51.accounting.dto.ClientRolesDto;
import telran.java51.accounting.dto.ClientUpdateDto;

public interface ClientAccountService {
	
	ClientDto register(ClientRegisterDto clientRegisterDto);
	
	ClientDto getUser(String login);
	
	ClientDto removeUser(String login);
	
	ClientDto updateUser(String login, ClientUpdateDto clientUpdateDto);
	
	ClientRolesDto addRole(String login, String role);
	
	ClientRolesDto removeRole(String login, String role);
	
	void changePassword(String login, String newPassword);

	void recoveryPasswordLink(String email);

	void recoveryPassword(String token, String password);
	
}
