package telran.java51.accounting.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@EqualsAndHashCode(of = "login")
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserAccount implements Serializable {

	private static final long serialVersionUID = -3381330667151788505L;
	
	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "login"))
	@Enumerated(EnumType.STRING)
    private Set<Role> roles;
	
	 public UserAccount() {
	    	roles = new HashSet<>();
			addRole(Role.USER.name());
		}
	
	 public UserAccount( String login, String password, String firstName, String lastName ) {
			this();
	    	this.login = login;
	    	this.password = password;
			this.firstName = firstName;
			this.lastName = lastName;
		}

	@Id
	String login;
	@Setter
	String password;
	@Setter
	 String firstName;
	@Setter
	String lastName;
	
	public boolean addRole(String role) {
		return roles.add(Role.valueOf(role.toUpperCase()));
	}

	public boolean removeRole(String role) {
		return roles.remove(Role.valueOf(role.toUpperCase()));
	}
	
}
