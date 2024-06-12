package telran.java51.accounting.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class AuthorizationConfiguration {
	
	@Bean
	public SecurityFilterChain web(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults());
		http.csrf(csrf -> csrf.disable());//
		http.headers().frameOptions().disable(); //for H2 render	
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));// add cookies and session authn			
		http.authorizeHttpRequests(authorize -> authorize				
				.dispatcherTypeMatchers( DispatcherType.ERROR ).permitAll() //for correct response if case exception(409,403 etc)
				.requestMatchers("/account/register", "/h2-console/**")
				.permitAll()	
				.anyRequest().authenticated());		
		return http.build();
	}
}
