package org.robbins.moviefinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MovieFinderApplication extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests(a -> a
						.antMatchers("/person/**", "/movie/**", "/error").permitAll()
						.anyRequest().authenticated())
				// .exceptionHandling(e -> e
				// 		.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()

				// .logout(l -> l
				// 		.logoutSuccessUrl("/").permitAll())

				.oauth2ResourceServer(oauth2 -> oauth2.jwt());
	}

	public static void main(String[] args) {
		SpringApplication.run(MovieFinderApplication.class, args);
	}

}
