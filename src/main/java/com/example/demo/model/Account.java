package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
@NoArgsConstructor
@Data
public class Account {
    @Id
    private int id;
    private int value;


}
