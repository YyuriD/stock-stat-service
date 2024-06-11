package telran.java51.accounting.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

@Table(name = "clients")
public class ClientAccount extends UserAccount {	
	
	private static final long serialVersionUID = 2888804168974627125L;
	
    String firstName;
    String lastName;
   
    @ElementCollection(targetClass = ClientRole.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "client_roles", joinColumns = @JoinColumn(name = "login"))
	@Enumerated(EnumType.STRING)
    private Set<ClientRole> roles;
    
    public ClientAccount() {
    	roles = new HashSet<>();
		addRole(ClientRole.FREE.name());
	}
    
    public ClientAccount( String login, String password, String firstName, String lastName ) {
		this();
    	this.login = login;
    	this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
    
    public boolean addRole(String role) {
		return roles.add(ClientRole.valueOf(role.toUpperCase()));
	}

	public boolean removeRole(String role) {
		return roles.remove(ClientRole.valueOf(role.toUpperCase()));
	}
 
}
