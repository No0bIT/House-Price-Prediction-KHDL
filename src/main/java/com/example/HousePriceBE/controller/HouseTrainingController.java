package com.example.HousePriceBE.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HousePriceBE.model.Direction;
import com.example.HousePriceBE.model.HouseCus;
import com.example.HousePriceBE.model.HouseTraining;
import com.example.HousePriceBE.model.Investor;
import com.example.HousePriceBE.model.Location;
import com.example.HousePriceBE.repository.CustomerRepository;
import com.example.HousePriceBE.repository.DirectionRepository;
import com.example.HousePriceBE.repository.HouseCusRepository;
import com.example.HousePriceBE.repository.HouseTrainingRepository;
import com.example.HousePriceBE.repository.InvestorRepository;
import com.example.HousePriceBE.repository.LocationRepository;

@RestController
@RequestMapping("/api/housetraining")
public class HouseTrainingController {

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	HouseTrainingRepository houseTrainingRepository;
	@Autowired
	HouseCusRepository houseCusRepository;

	@Autowired
	LocationRepository locationRepository;
	@Autowired
	InvestorRepository investorRepository;
	@Autowired
	DirectionRepository directionRepository;
	
	
	@PostMapping("/addData")
	public boolean addData(@RequestBody HouseCus houseTraining) throws IOException {
		HouseCus hs= houseCusRepository.getById(houseTraining.getId());
		hs.setStatus(1);
		hs.setPrice(houseTraining.getPrice());
		houseCusRepository.save(hs);
		HouseTraining ht = new HouseTraining();
		ht.setLocation(houseTraining.getLocation());
		ht.setSize(houseTraining.getSize());
		ht.setHouseDirection(houseTraining.getHouseDirection());
		ht.setBalconyDirection(houseTraining.getBalconyDirection());
		ht.setBedrooms(houseTraining.getBedrooms());
		ht.setBathrooms(houseTraining.getBathrooms());
		ht.setInvestor(houseTraining.getInvestor());
		ht.setPrice(houseTraining.getPrice());
		houseTrainingRepository.save(ht);
		
		 String filePath = "/Users/untiv/Desktop/NMKHDL/HousePriceBE/dataTraining.arff";
		 int loc=0;
		 
		 
		 int check=0;
         List<Location> locations = locationRepository.findAll();
         for(Location location : locations) {
         	if(location.getLocation().equals(houseTraining.getLocation())){
         		loc =location.getId();
         		check=1;
         		break;
         	}
         }
         if(check==0) {
         	Location l = new Location();
         	l.setLocation(houseTraining.getLocation());
         	locationRepository.save(l);
         	loc = l.getId(); 
         }
         else
         	check=0;
		 
         int inv=0;
         List<Investor> investors = investorRepository.findAll();
         for(Investor investor : investors) {
         	if(investor.getName().equals(houseTraining.getInvestor())){
         		inv =investor.getId();
         		check=1;
         		break;
         	}
         }
         if(check==0) {
         	Investor i = new Investor();
         	i.setName(houseTraining.getInvestor());
         	investorRepository.save(i);
         	inv = i.getId(); 
         }
         int hou=0;
         List<Direction> directions = directionRepository.findAll();
         for(Direction direction : directions) {
         	if(direction.getDirection().equals(houseTraining.getBalconyDirection())){
         		hou =direction.getId();
         		break;
         	}
         }
         int ban=0;
         for(Direction direction : directions) {
         	if(direction.getDirection().equals(houseTraining.getBalconyDirection())){
         		hou =direction.getId();
         		break;
         	}
         }
         
         
		

		 String arffData = loc+","+houseTraining.getSize()+","+ hou+","+ban+","+houseTraining.getBedrooms()+","+houseTraining.getBathrooms()+","+inv +","+houseTraining.getPrice();

	        // Ghi dữ liệu mới vào cuối tệp ARFF
		 try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
			    writer.append(arffData + "\n"); // Thêm ký tự xuống dòng để tạo dòng mới
			    System.out.println("Đã ghi dữ liệu vào file ARFF.");
			} catch (IOException e) {
			    System.out.println("Lỗi khi ghi dữ liệu vào file ARFF: " + e.getMessage());
			    e.printStackTrace(); // In stack trace để xác định lỗi cụ thể
			    return false; // Trả về false nếu có lỗi
			}        
		return true;
	}
}
