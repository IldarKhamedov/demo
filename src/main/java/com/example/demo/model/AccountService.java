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

    public void addAccount(int id, int value) {
        Account account = new Account();
        account.setId(id);
        account.setValue(value);
        accountRepository.save(account);
    }

    public void deleteAccount(int id) {
        accountRepository.delete(accountRepository.getById(id));
    }

    public void editAccount(int id, int value) {
        Account account = accountRepository.findById(id).orElse(new Account());
        account.setValue(value);
        accountRepository.save(account);
    }


}
