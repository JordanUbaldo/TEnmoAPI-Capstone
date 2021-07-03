package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferService {

        public static String AUTH_TOKEN = "";
        private final String BASE_URL;
        public RestTemplate restTemplate = new RestTemplate();

        public TransferService(String url) {
            BASE_URL = url;
        }

        public void transfer(AuthenticatedUser user) {
            Scanner scan = new Scanner(System.in);
            System.out.println("\nEnter ID of user you are sending to (0 to cancel):");
            String toString = scan.nextLine();
            if (toString.equals("0")) {
                System.out.println("\nTransfer Canceled");
            } else {
                int to = Integer.parseInt(toString);
                System.out.println("\nEnter amount:");
                String amountString = scan.nextLine();
                BigDecimal amount = new BigDecimal(amountString);

                Transfer transfer = new Transfer();
                transfer.setAmount(amount);
                transfer.setToUserId(to);
                transfer.setFromUserId(user.getUser().getId());
                transfer.setStatusDesc("Approved");
                transfer.setTypeDesc("Send");
                transfer.setStatusId(2);
                transfer.setTypeId(2);

                restTemplate.exchange(BASE_URL + "/transfer", HttpMethod.POST,
                        makeTransferEntity(user, transfer), Transfer.class).getBody();

            }
        }

    public void getTransfers(AuthenticatedUser authUser) {
        Transfer[] transfers = null;
        transfers = restTemplate.exchange(BASE_URL + "transfer/account", HttpMethod.GET,
                makeAuthEntity(authUser.getToken()), Transfer[].class).getBody();

        System.out.println();
        System.out.println("Transfers:");
        for (int i = 0; i < transfers.length; i++) {
            System.out.println("ID: " + transfers[i].getTransferId() + " From: " + transfers[i].getFromUserName() + ", To: " + transfers[i].getToUserName() + " Amount: $" + transfers[i].getAmount());
        }

        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter transfer ID to view details (0 to cancel):");
        String transferIdString = scan.nextLine();
        if (!transferIdString.equals("0")) {
            int transferId = Integer.parseInt(transferIdString);
            Transfer transfer = null;
            transfer = restTemplate.exchange(BASE_URL + "transfer/" + transferId, HttpMethod.GET, makeAuthEntity(authUser.getToken()), Transfer.class).getBody();

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
    }


        private HttpEntity<Transfer> makeTransferEntity(AuthenticatedUser user, Transfer transfer) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(user.getToken());
            HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
            return entity;
        }

            private HttpEntity makeAuthEntity(String token) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity entity = new HttpEntity<>(headers);
            return entity;
        }

    }

