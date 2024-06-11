package telran.java51.accounting.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java51.accounting.dto.ClientDto;
import telran.java51.accounting.dto.ClientRegisterDto;
import telran.java51.accounting.dto.ClientRolesDto;
import telran.java51.accounting.dto.ClientUpdateDto;
import telran.java51.accounting.service.ClientAccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class ClientAccountController {
	
	final ClientAccountService clientAccountService;
	
	@PostMapping("/register")
	ClientDto register(@RequestBody ClientRegisterDto clientRegisterDto) {
		return clientAccountService.register(clientRegisterDto);
	}
	
	@PostMapping("/login")
	ClientDto login(Principal principal) {
		return clientAccountService.getUser(principal.getName());
	}


	@DeleteMapping("/user/{login}")
	public ClientDto removeUser(@PathVariable String login) {
		return clientAccountService.removeUser(login);
	}

	@PutMapping("/user/{login}")
	public ClientDto updateUser(@PathVariable String login, @RequestBody ClientUpdateDto clientUpdateDto) {
		return clientAccountService.updateUser(login, clientUpdateDto);
	}

	@PutMapping("/user/{login}/role/{role}")
	public ClientRolesDto addRole(@PathVariable String login, @PathVariable String role) {
		return clientAccountService.addRole(login, role);
	}

	@DeleteMapping("/user/{login}/role/{role}")
	public ClientRolesDto removeRole(@PathVariable String login, @PathVariable String role) {
		return clientAccountService.removeRole(login, role);
	}

	@PutMapping("/password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void changePassword(Principal principal, @RequestHeader("X-Password") String newPassword) {
		clientAccountService.changePassword(principal.getName(), newPassword);	
	}
	
	@GetMapping("/recovery/{email}")
	public void recoveryPasswordLink(@PathVariable String email) {
		clientAccountService.recoveryPasswordLink(email);
	}
	
	@PostMapping("/recovery/{token}")
	public void recoveryPassword(@PathVariable String token, @RequestHeader("X-Password") String password) {
		clientAccountService.recoveryPassword(token, password);
	}

}
