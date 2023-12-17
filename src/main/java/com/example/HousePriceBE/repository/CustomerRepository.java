package com.example.HousePriceBE.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HousePriceBE.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}
