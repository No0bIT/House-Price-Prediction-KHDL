package com.example.HousePriceBE.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HousePriceBE.model.Customer;
import com.example.HousePriceBE.repository.CustomerRepository;
import com.example.HousePriceBE.subModel.Login;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
	

	@Autowired
	CustomerRepository customerRepository;
	
	@PostMapping("/add")
	public Customer addCus(@RequestBody Customer customer) {
		List<Customer> customers= customerRepository.findAll();
		for(Customer cus:customers) {
			if(cus.getUsername().equals(customer.getUsername()))
			{
				if(cus.getPassword().equals(customer.getPassword()))
					return null;
			}
		}
		customerRepository.save(customer);
		return customer;
	}
	@PostMapping("/login")
	public Customer getloggin(@RequestBody Login login) {
		List<Customer> customers= customerRepository.findAll();
		for(Customer cus:customers) {
			if(cus.getUsername().equals(login.getUsername()))
			{
				if(cus.getPassword().equals(login.getPassword()))
					return cus;
			}
		}
		return null;
	}
}
