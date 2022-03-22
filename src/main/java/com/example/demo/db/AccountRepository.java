package com.example.demo.db;

import com.example.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {


    //List<Account> findAllWhereValueGte(int value);

}
