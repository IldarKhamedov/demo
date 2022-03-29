package com.example.demo.api;

import com.example.demo.model.AccountService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("info")
    public AccountInfoResponse accountInfo(@RequestBody AccountInfoRequest request) {
        int id = request.id;
        AccountService.AccountDto account = accountService.getAccount(id);
        return new AccountInfoResponse(
                account.getId(),
                account.getUsdValue(),
                account.getRubValue()
        );
    }
/*
    @GetMapping("infoJson")
    public AccountInfoJsonResponse accountInfoJson(@RequestBody AccountInfoRequest request) {
        int id = request.id;
        AccountService.AccountJsonDto account = accountService.getAccountJson(id);
        return new AccountInfoJsonResponse(
                account.getId(),
                account.getUsdValue(),
                account.getRubValue()
        );
    }*/

    @Data
    public static class AccountInfoRequest {
        private int id;
        private int value;

    }

    @Data
    public static class AccountInfoJsonRequest {
        private int id;
        private int value;
    }

    @RequiredArgsConstructor
    public static class AccountInfoResponse {
        public final int id;
        public final BigDecimal value;
        public final BigDecimal rubValue;
    }
    /*
    @RequiredArgsConstructor
    public static class AccountInfoJsonResponse {
        public final int id;
        public final Double value;
        public final Double rubValue;
    }*/

    @PostMapping("addAccount")
    public AccountAddResponse addAccount(@RequestBody AccountAddRequest addRequest) {
        accountService.addAccount(addRequest.getId(), addRequest.getValue());
        return new AccountAddResponse(
                addRequest.getId(),
                addRequest.getValue()
        );

    }

    @Data
    public static class AccountAddRequest {
        private int id;
        private int value;
    }

    @RequiredArgsConstructor
    public static class AccountAddResponse {
        public final int id;
        public final int value;
    }

    @DeleteMapping("remove")
    public void deleteAccount(@RequestBody AccountDeleteRequest accountDeleteRequest) {
        accountService.deleteAccount(accountDeleteRequest.getId());
    }

    @Data
    public static class AccountDeleteRequest {
        private int id;
        private int value;
    }

    @RequiredArgsConstructor
    public static class AccountDeleteResponse {
        public final int id;
        public final int value;
    }

   /* @PutMapping("edit")
    public AccountEditResponse editAccount(@RequestBody AccountAddRequest request) {
       AccountEditResponse accountEditResponse = new AccountEditResponse();
        Account account = accountService.getAccount(request.getId());
        accountEditResponse.setId(request.getId());
        accountEditResponse.setOldValue(account.getValue());
        accountEditResponse.setNewValue(request.getValue());
        accountService.editAccount(request.getId(), request.getValue());
        return accountEditResponse;
    }*/

    @Data
    public static class AccountEditRequest {
        private int id;
        private int value;
    }

    @Data

    @NoArgsConstructor
    public static class AccountEditResponse {
        public int id;
        public int oldValue;
        public int newValue;
    }

}
