package telran.java51.accounting.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import telran.java51.accounting.model.Role;

@Configuration
@RequiredArgsConstructor
public class AuthorizationConfiguration {
	
	@Bean
	public SecurityFilterChain web(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults());
		http.cors(cors->cors.disable());
		http.csrf(csrf -> csrf.disable());//
		http.headers().frameOptions().disable(); //for H2 render	
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));// add cookies and session authn			
		http.authorizeHttpRequests(authorize -> authorize				
				.dispatcherTypeMatchers( DispatcherType.ERROR ).permitAll() //for correct response if case exception(409,403 etc)
				.requestMatchers("/account/register", "/h2-console/**")
				.permitAll()	
				.requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
				.access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMIN')"))
				.requestMatchers(HttpMethod.PUT, "/account/user/{login}")
				.access(new WebExpressionAuthorizationManager("#login == authentication.name"))
				.requestMatchers("/account/user/{login}/role/{role}").hasRole(Role.ADMIN.name())
				
				.anyRequest().authenticated());		
		return http.build();
	}
	
	 @Bean
	    CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(Arrays.asList("*"));
	        configuration.setAllowedMethods(Arrays.asList("*"));
	        configuration.setAllowedHeaders(Arrays.asList("*"));
	        configuration.setAllowCredentials(true);
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }
}
