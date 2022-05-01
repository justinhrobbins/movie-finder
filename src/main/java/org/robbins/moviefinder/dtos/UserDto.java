package org.robbins.moviefinder.dtos;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
    private String email;
    private String name;
    private List<String> streamingServices = new ArrayList<>();
    
    public UserDto() {
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getStreamingServices() {
        return streamingServices;
    }
    public void setStreamingServices(List<String> streamingServices) {
        this.streamingServices = streamingServices;
    }

    @Override
    public String toString() {
        return "UserDto [email=" + email + ", name=" + name + ", streamingServices=" + streamingServices + "]";
    }
}
