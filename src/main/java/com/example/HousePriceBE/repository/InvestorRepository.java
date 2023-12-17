package com.example.HousePriceBE.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HousePriceBE.model.Investor;

public interface InvestorRepository extends JpaRepository<Investor, Integer> {

}
