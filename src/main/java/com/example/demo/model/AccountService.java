package com.example.demo.model;

import com.example.demo.db.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account getAccount(int id) {
        return accountRepository.getById(id);
    }

}
