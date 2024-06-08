package telran.java51.clientAccount.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRolesDto {
	
	String login;
	Set<String> roles;
}
