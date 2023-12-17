package com.example.HousePriceBE.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HousePriceBE.model.Customer;
import com.example.HousePriceBE.model.HouseCus;
import com.example.HousePriceBE.repository.CustomerRepository;
import com.example.HousePriceBE.repository.HouseCusRepository;

@RestController
@RequestMapping("/api/housecus")
public class HouseCusController {

	@Autowired
	HouseCusRepository houseCusRepository;
	@Autowired
	CustomerRepository customerRepository;
	@PostMapping("/add/{id}")
	public Customer addHouseCus(@RequestBody HouseCus houseCus,@PathVariable int id)
	{
		Customer customer=customerRepository.getById(id);
		houseCus.setCustomer(customer);
		houseCusRepository.save(houseCus);
		return customerRepository.getById(id);
	}
	@GetMapping("/gethouseCusUser/{id}")
	public List<HouseCus> getHouseCuss(@PathVariable int id){
		Customer cus= customerRepository.getById(id);
		return cus.getHouseCuss();
	}
}
