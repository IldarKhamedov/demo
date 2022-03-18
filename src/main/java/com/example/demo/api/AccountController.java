package com.example.demo.api;

import com.example.demo.model.Account;
import com.example.demo.model.AccountService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("info")
    public AccountInfoResponse accountInfo(@RequestBody AccountInfoRequest request) {
        int id = request.id;
        Account account = accountService.getAccount(id);
        return new AccountInfoResponse(
                account.getId(),
                account.getValue()
        );
    }

    @Data
    public static class AccountInfoRequest {
        private int id;
    }

    @RequiredArgsConstructor
    public static class AccountInfoResponse {
        public final int id;
        public final int value;
    }

}
