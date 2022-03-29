package com.example.demo.model;

import com.example.demo.db.AccountRepository;
import com.example.demo.integration.CurrencyClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyClient currencyClient;

   /* private final JsonClient jsonClient;

    public AccountJsonDto getAccountJson(int id) {
        Account account = accountRepository.getById(id);
        Double usdValue = Double.valueOf(account.getValue());
        Map<String,String> stringMap=jsonClient.getCurrencyRates().get("USD");
        Double rubValue=Double.valueOf(stringMap.get("Value"));
        return new AccountJsonDto(account.getId(), usdValue, usdValue*rubValue);
    }*/

    public AccountDto getAccount(int id) {
        Account account = accountRepository.getById(id);
        BigDecimal usdValue = BigDecimal.valueOf(account.getValue());
        BigDecimal rubValue = currencyClient.getCurrencyRates().get("USD");

        return new AccountDto(account.getId(), usdValue, usdValue.divide(rubValue, RoundingMode.CEILING));
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

    @Data
    @AllArgsConstructor
    public static class AccountDto {
        private int id;
        private BigDecimal usdValue;
        private BigDecimal rubValue;
    }

    @Data
    @AllArgsConstructor
    public static class AccountJsonDto {
        private int id;
        private Double usdValue;
        private Double rubValue;
    }

}
