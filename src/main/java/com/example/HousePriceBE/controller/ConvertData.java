package com.example.HousePriceBE.controller;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HousePriceBE.model.Direction;
import com.example.HousePriceBE.model.HouseTraining;
import com.example.HousePriceBE.model.Investor;
import com.example.HousePriceBE.model.Location;
import com.example.HousePriceBE.repository.DirectionRepository;
import com.example.HousePriceBE.repository.HouseTrainingRepository;
import com.example.HousePriceBE.repository.InvestorRepository;
import com.example.HousePriceBE.repository.LocationRepository;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

@RestController
@RequestMapping("/DataTraining")
public class ConvertData {
	
	@Autowired
	HouseTrainingRepository houseTrainingRepository;
	@Autowired
	LocationRepository locationRepository;
	@Autowired
	InvestorRepository investorRepository;
	@Autowired
	DirectionRepository directionRepository;
	
	@GetMapping("/convertfile")
	public void convertFile() {
		File f= new File("/Users/untiv/Desktop/NMKHDL/HousePriceBE/data.txt");
		try {
			BufferedReader file= Files.newBufferedReader(f.toPath(), StandardCharsets.UTF_8);	
			while(true) {
				String line= file.readLine();
				if(line == null)
					break;
				String []datas=line.split(",");
				
				HouseTraining houseTraining= new HouseTraining();
				houseTraining.setLocation(datas[0]);
				houseTraining.setSize(Float.parseFloat(datas[1]));
				houseTraining.setHouseDirection(datas[2]);
				houseTraining.setBalconyDirection(datas[3]);
				if(datas[4].equals("None")) 
					houseTraining.setBedrooms(Math.round(houseTraining.getSize()/30));
				else
					houseTraining.setBedrooms(Integer.parseInt(datas[4]));
				//
				if(datas[5].equals("None")) 
					houseTraining.setBathrooms(houseTraining.getBedrooms());
				else
					houseTraining.setBathrooms(Integer.parseInt(datas[5]));
				//
				if(datas[6].equals("None")) 
					houseTraining.setInvestor("Nhỏ Lẻ");
				else
					houseTraining.setInvestor(datas[6]);
				
				houseTraining.setPrice(Float.parseFloat(datas[7]));
				houseTrainingRepository.save(houseTraining);
			}	
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@GetMapping("/clastodata")
	public void clastodata() {
		ArrayList<Attribute> attributes = new ArrayList<>();
		attributes.add(new Attribute("location"));
        attributes.add(new Attribute("size"));
        attributes.add(new Attribute("houseDirection"));
        attributes.add(new Attribute("balconyDirection"));
        attributes.add(new Attribute("bedrooms"));
        attributes.add(new Attribute("bathrooms"));
        attributes.add(new Attribute("investor"));
        attributes.add(new Attribute("price"));
        
        Instances data = new Instances("HouseData", attributes, 0);
        
        List<HouseTraining> houseList= houseTrainingRepository.findAll();
        
        for (HouseTraining house : houseList) {
            double[] instanceValues = new double[data.numAttributes()];
            int check=0;
            List<Location> locations = locationRepository.findAll();
            for(Location location : locations) {
            	if(location.getLocation().equals(house.getLocation())){
            		instanceValues[0] =location.getId();
            		check=1;
            		break;
            	}
            }
            if(check==0) {
            	Location l = new Location();
            	l.setLocation(house.getLocation());
            	locationRepository.save(l);
            	instanceValues[0] = l.getId(); 
            }
            else
            	check=0;
            
            
            instanceValues[1] = house.getSize();
            
            List<Direction> directions = directionRepository.findAll();
            for(Direction direction : directions) {
            	if(direction.getDirection().equals(house.getHouseDirection())){
            		instanceValues[2] =direction.getId();
            		check=1;
            		break;
            	}
            }
            if(check==0) {
            	Direction d = new Direction();
            	d.setDirection(house.getHouseDirection());
            	directionRepository.save(d);
            	instanceValues[2] = d.getId(); 
            }
            else
            	check=0;
            
            
            for(Direction direction : directions) {
            	if(direction.getDirection().equals(house.getBalconyDirection())){
            		instanceValues[3] =direction.getId();
            		check=1;
            		break;
            	}
            }
            if(check==0) {
            	Direction d = new Direction();
            	d.setDirection(house.getBalconyDirection());
            	directionRepository.save(d);
            	instanceValues[3] = d.getId(); 
            }
            else
            	check=0;
            instanceValues[4] = house.getBedrooms();
            instanceValues[5] = house.getBathrooms();
            
            List<Investor> investors = investorRepository.findAll();
            for(Investor investor : investors) {
            	if(investor.getName().equals(house.getInvestor())){
            		instanceValues[6] =investor.getId();
            		check=1;
            		break;
            	}
            }
            if(check==0) {
            	Investor i = new Investor();
            	i.setName(house.getInvestor());
            	investorRepository.save(i);
            	instanceValues[6] = i.getId(); 
            }
            
            instanceValues[7] = house.getPrice();

            
            data.add(new DenseInstance(1.0, instanceValues));
        }
        ArffSaver arffSaver = new ArffSaver();
        arffSaver.setInstances(data);

        try {
            arffSaver.setFile(new File("/Users/untiv/Desktop/NMKHDL/HousePriceBE/dataTraining.arff"));
            arffSaver.writeBatch();
            System.out.println("Dữ liệu đã được ghi vào file DataTraining.arff.");
        } catch (IOException e) {
            e.printStackTrace();
        }   
	}
}
