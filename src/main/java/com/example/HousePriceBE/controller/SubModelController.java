package com.example.HousePriceBE.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HousePriceBE.model.Direction;
import com.example.HousePriceBE.model.Investor;
import com.example.HousePriceBE.model.Location;
import com.example.HousePriceBE.repository.DirectionRepository;
import com.example.HousePriceBE.repository.InvestorRepository;
import com.example.HousePriceBE.repository.LocationRepository;

@RequestMapping("/api/sub")
@RestController
public class SubModelController {

	@Autowired
	DirectionRepository directionRepository;
	@Autowired
	LocationRepository locationRepository;
	@Autowired
	InvestorRepository investorRepository;
	
	@PostMapping("/add")
	public Direction addD(@RequestBody Direction d)
	{
		directionRepository.save(d);
		return d;
	}
	@GetMapping("/getLocation")
	public List<Location> getL(){
		return locationRepository.findAll();
	}
	@GetMapping("/getInvestor")
	public List<Investor> getI(){
		return investorRepository.findAll();
	}
	@GetMapping("/getDirection")
	public List<Direction> getD(){
		return directionRepository.findAll();
	}
}
