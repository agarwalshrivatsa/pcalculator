package com.pmspod.pcalculator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Account {

    @Id
    @Column(name = "account_id")
    private String accountId;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "accountId")
    private List<Trade> trades;

}
