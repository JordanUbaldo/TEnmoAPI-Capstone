package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferService {

        private final String BASE_URL;
        public RestTemplate restTemplate = new RestTemplate();

        public TransferService(String url) {
            BASE_URL = url;
        }

        public void transfer(AuthenticatedUser user) {
            User[] users = null;
            try {
                users = restTemplate.exchange(BASE_URL + "/user", HttpMethod.GET,
                        makeAuthEntity(user), User[].class).getBody();

                System.out.println("Users");
                System.out.println("ID\t\t Name");
                for (int i = 0; i < users.length; i++) {
                    System.out.println(users[i].getId() + "\t" + users[i].getUsername());
                }
            } catch (RestClientException e) {
                System.out.println("Unable to retrieve Users!");
            }

            Scanner scan = new Scanner(System.in);
            System.out.println("\nEnter ID of user you are sending to (0 to cancel):");
            String toString = scan.nextLine();
            if (toString.equals("0")) {
                System.out.println("\nTransfer Canceled");
            } else {
                int to = 0;
                try {
                    to = Integer.parseInt(toString);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid Input");
                }
                System.out.println("\nEnter amount:");
                String amountString = scan.nextLine();
                BigDecimal amount = new BigDecimal(0);
                try {
                    amount = new BigDecimal(amountString);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid Input");
                }
                Transfer transfer = new Transfer();
                transfer.setAmount(amount);
                transfer.setToUserId(to);
                transfer.setFromUserId(user.getUser().getId());
                transfer.setStatusDesc("Approved");
                transfer.setTypeDesc("Send");
                transfer.setStatusId(2);
                transfer.setTypeId(2);

                try {
                    restTemplate.exchange(BASE_URL + "/transfer", HttpMethod.POST,
                            makeTransferEntity(user, transfer), Transfer.class).getBody();
                } catch (Exception e) {
                    System.out.println("Invalid User");
                }
            }
        }

    public void getTransfers(AuthenticatedUser authUser) {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.exchange(BASE_URL + "transfer/account", HttpMethod.GET,
                    makeAuthEntity(authUser), Transfer[].class).getBody();

            System.out.println();
            System.out.println("Transfers:");
            for (int i = 0; i < transfers.length; i++) {
                System.out.println("ID: " + transfers[i].getTransferId() + " From: " + transfers[i].getFromUserName() + ", To: " + transfers[i].getToUserName() + " Amount: $" + transfers[i].getAmount());
            }
            try {
                Scanner scan = new Scanner(System.in);
                System.out.println("Please enter transfer ID to view details (0 to cancel):");
                String transferIdString = scan.nextLine();
                if (!transferIdString.equals("0")) {
                    int transferId = Integer.parseInt(transferIdString);
                   Transfer transfer = restTemplate.exchange(BASE_URL + "transfer/" + transferId, HttpMethod.GET, makeAuthEntity(authUser), Transfer.class).getBody();

                    System.out.println();
                    System.out.println("Transfer Details");
                    System.out.println();
                    System.out.println("Id: " + transfer.getTransferId());
                    System.out.println("From: " + transfer.getFromUserName());
                    System.out.println("To: " + transfer.getToUserName());
                    System.out.println("Type: " + transfer.getTypeDesc());
                    System.out.println("Status: " + transfer.getStatusDesc());
                    System.out.println("Amount: $" + transfer.getAmount());
                }
            } catch (RestClientException ex) {
                System.out.println("Unable to retrieve transfer details!");
            }
        } catch (RestClientException e) {
            System.out.println("Unable to retrieve transfers!");
        }
    }


        private HttpEntity<Transfer> makeTransferEntity(AuthenticatedUser user, Transfer transfer) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(user.getToken());
            HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
            return entity;
        }

            private HttpEntity makeAuthEntity(AuthenticatedUser user) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(user.getToken());
            HttpEntity entity = new HttpEntity<>(headers);
            return entity;
        }

    }

