package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;

public class AccountService {
    public static String AUTH_TOKEN = "";
    private final String BASE_URL;
    public RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url) {
        BASE_URL = url;
    }

    public BigDecimal balance(AuthenticatedUser user) {
        BigDecimal balance;
//      try {
            balance = restTemplate.exchange(BASE_URL + "accounts/balance", HttpMethod.GET,
                    makeAuthEntity(user.getToken()), BigDecimal.class).getBody();
//
//        } catch (RestClientResponseException ex) {
//          throw new AccountNotFoundException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
//        }
        return balance;
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
