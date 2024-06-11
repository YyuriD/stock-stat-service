package telran.java51.accounting.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@EqualsAndHashCode(of = "login")
@NoArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserAccount implements Serializable {

	private static final long serialVersionUID = -3381330667151788505L;
	
	public UserAccount(String login, String password) {
		this.login = login;
		this.password = password;
	}

	@Id
//	@Length(min = 3, message = "The login must be at least 6 symbols")
//	@Length(max = 40, message = "The login must be less than 40 symbols")
	String login;
	
	@Setter
	String password;
	
}
