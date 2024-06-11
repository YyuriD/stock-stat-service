package telran.java51.accounting.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.accounting.dao.UserAccountRepository;
import telran.java51.accounting.model.ClientAccount;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	final UserAccountRepository userAccountRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername " +  username);
		ClientAccount clientAccount = userAccountRepository.findClientByLogin(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
		Collection<String> authorities = clientAccount.getRoles()
				.stream()
				.map(r -> "ROLE_" + r.name())
				.collect(Collectors.toList());
		return new User(username, clientAccount.getPassword(), AuthorityUtils.createAuthorityList(authorities));
	}

}
