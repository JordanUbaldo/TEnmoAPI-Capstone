package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class UserService {
    private final String BASE_URL;
    public RestTemplate restTemplate = new RestTemplate();

    public UserService(String url) {
        BASE_URL = url;
    }


    //Can remove
    public void getUsers(AuthenticatedUser authUser) {
        User[] users = null;
        try {
            users = restTemplate.exchange(BASE_URL + "/user", HttpMethod.GET,
                    makeAuthEntity(authUser), User[].class).getBody();

            System.out.println("Users");
            System.out.println("ID\t\t Name");
            for (int i = 0; i < users.length; i++) {
                System.out.println(users[i].getId() + "\t" + users[i].getUsername());
            }
        } catch (RestClientException e) {
            System.out.println("Unable to retrieve Users!");
        }
    }

    private HttpEntity<User> makeUserEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        HttpEntity<User> entity = new HttpEntity<>(user.getUser(), headers);
        return entity;
    }

    private HttpEntity makeAuthEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
