package org.robbins.moviefinder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "tmdb")
public class TmdbConfigProperties {
    private String apikey;

    public TmdbConfigProperties(final String apikey) {
        this.apikey = apikey;
    }

    public String getApikey() {
        return apikey;
    }
}
