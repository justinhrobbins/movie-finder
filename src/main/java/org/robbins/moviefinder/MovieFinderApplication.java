package org.robbins.moviefinder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MovieFinderApplication extends WebSecurityConfigurerAdapter {

	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
	String jwkSetUri;

	@Bean
	public JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}

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
				.cors()
				.and()
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
	}

	public static void main(String[] args) {
		SpringApplication.run(MovieFinderApplication.class, args);
	}

}
