package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    public static String AUTH_TOKEN = "";
    private final String BASE_URL;
    public RestTemplate restTemplate = new RestTemplate();

    public UserService(String url) {
        BASE_URL = url;
    }

    public void getUsers(AuthenticatedUser authUser) {
        User[] users = null;
        users = restTemplate.exchange(BASE_URL + "/user", HttpMethod.GET,
                makeAuthEntity(authUser.getToken()), User[].class).getBody();

        for (int i = 0; i< users.length; i++) {
            System.out.println(users[i].getId() + "\t" + users[i].getUsername());
        }
    }

    private HttpEntity<User> makeUserEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        HttpEntity<User> entity = new HttpEntity<>(user.getUser(), headers);
        return entity;
    }

    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
