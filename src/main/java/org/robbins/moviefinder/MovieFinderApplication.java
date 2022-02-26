package org.robbins.moviefinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MovieFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieFinderApplication.class, args);
	}
}
