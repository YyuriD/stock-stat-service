package telran.java51.accounting.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {

	String login;
    String firstName;
    String lastName;
    @Singular
    Set<String> roles;
}
