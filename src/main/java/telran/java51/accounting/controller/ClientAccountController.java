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
import telran.java51.accounting.dto.UserDto;
import telran.java51.accounting.dto.UserRegisterDto;
import telran.java51.accounting.dto.UserRolesDto;
import telran.java51.accounting.dto.UserUpdateDto;
import telran.java51.accounting.service.UserAccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class ClientAccountController {
	
	final UserAccountService userAccountService;
	
	@PostMapping("/register")
	UserDto register(@RequestBody UserRegisterDto clientRegisterDto) {
		return userAccountService.register(clientRegisterDto);
	}
	
	@PostMapping("/login")
	UserDto login(Principal principal) {
		return userAccountService.getUser(principal.getName());
	}


	@DeleteMapping("/user/{login}")
	public UserDto removeUser(@PathVariable String login) {
		return userAccountService.removeUser(login);
	}

	@PutMapping("/user/{login}")
	public UserDto updateUser(@PathVariable String login, @RequestBody UserUpdateDto clientUpdateDto) {
		return userAccountService.updateUser(login, clientUpdateDto);
	}

	@PutMapping("/user/{login}/role/{role}")
	public UserRolesDto addRole(@PathVariable String login, @PathVariable String role) {
		return userAccountService.addRole(login, role);
	}

	@DeleteMapping("/user/{login}/role/{role}")
	public UserRolesDto removeRole(@PathVariable String login, @PathVariable String role) {
		return userAccountService.removeRole(login, role);
	}

	@PutMapping("/password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void changePassword(Principal principal, @RequestHeader("X-Password") String newPassword) {
		userAccountService.changePassword(principal.getName(), newPassword);	
	}
	
	@GetMapping("/recovery/{email}")
	public void recoveryPasswordLink(@PathVariable String email) {
		userAccountService.recoveryPasswordLink(email);
	}
	
	@PostMapping("/recovery/{token}")
	public void recoveryPassword(@PathVariable String token, @RequestHeader("X-Password") String password) {
		userAccountService.recoveryPassword(token, password);
	}

}
