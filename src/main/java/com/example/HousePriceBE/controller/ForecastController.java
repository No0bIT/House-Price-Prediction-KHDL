package com.example.HousePriceBE.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;

@RestController
@RequestMapping("/api/forecast")
public class ForecastController {
	
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
	@PostMapping("/get")
	public HouseCus getForecast(@RequestBody HouseCus h) throws Exception
	{

		 	DataSource source = new DataSource("/Users/untiv/Desktop/NMKHDL/HousePriceBE/dataTraining.arff");
	        Instances dataset = source.getDataSet();
	        
	        // Thiết lập thuộc tính dự đoán (price) là thuộc tính cuối cùng trong dataset
	        dataset.setClassIndex(dataset.numAttributes() - 1);

	        // Tạo mô hình cây quyết định
	        RandomForest randomForest = new RandomForest();
	        randomForest.setNumFeatures(100); // Số lượng cây quyết định
	        // Huấn luyện mô hình
	        randomForest.buildClassifier(dataset);
       
		Instance newHouse = new DenseInstance(8); // 8 là số thuộc tính trong data training 

		// Thiết lập giá trị cho các thuộc tính của căn nhà mới, ví dụ:
		 int check=0;
         List<Location> locations = locationRepository.findAll();
         for(Location location : locations) {
         	if(location.getLocation().equals(h.getLocation())){
        		newHouse.setValue(0, location.getId()); // Location
         		check=1;
         		break;
         	}
         }
         if(check==0) {
         	Location l = new Location();
         	l.setLocation(h.getLocation());
         	locationRepository.save(l);
         	newHouse.setValue(0, l.getId()); 
         }
         else
         	check=0;

		newHouse.setValue(1, h.getSize()); // Size
		
		List<Direction> directions = directionRepository.findAll();
        for(Direction direction : directions) {
        	if(direction.getDirection().equals(h.getHouseDirection())){
                newHouse.setValue(2,direction.getId()); // House Direction
        		break;
        	}
        }
        
        for(Direction direction : directions) {
        	if(direction.getDirection().equals(h.getBalconyDirection())){
                newHouse.setValue(3,direction.getId()); // Balcony Direction
        		break;
        	}
        }

		newHouse.setValue(4, h.getBedrooms()); // Bedrooms
		newHouse.setValue(5, h.getBathrooms()); // Bathrooms
		
		
		List<Investor> investors = investorRepository.findAll();
        for(Investor investor : investors) {
        	if(investor.getName().equals(h.getInvestor())){
        		newHouse.setValue(6, investor.getId()); // Investor
        		check=1;
        		break;
        	}
        }
        if(check==0) {
        	Investor i = new Investor();
        	i.setName(h.getInvestor());
        	investorRepository.save(i);
    		newHouse.setValue(6, i.getId()); // Investor 
        }
		newHouse.setMissing(7); // Giá sẽ được dự đoán, không cần thiết lập giá trị
		
		newHouse.setDataset(dataset);
		try {
		    double predictedPrice1 = randomForest.classifyInstance(newHouse); // Dự đoán giá RandomForest
		    
//		    double predictedPrice2 = svm.classifyInstance(newHouse); // Dự đoán giá SVM
//		    double predictedPrice3 = naiveBayes.classifyInstance(newHouse); // Dự đoán giá NaiveBayes
//		    double predictedPrice = (predictedPrice1+predictedPrice2+predictedPrice3)/3;	
		    double roundedNumber = Math.round(predictedPrice1 * 100.0) / 100.0;
		    h.setPrice((float)roundedNumber); 
		    return h;
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
	}
	
	// danh gia mo hinh
	
	@GetMapping("/getMeanSquared")
	public void getDanhGia() {
		try {
            // Đọc dữ liệu từ tệp ARFF
            ArffLoader loader = new ArffLoader();
            loader.setFile(new File("/Users/untiv/Desktop/NMKHDL/HousePriceBE/dataTraining.arff"));
            Instances data = loader.getDataSet();
            data.setClassIndex(data.numAttributes() - 1);

            // Xây dựng mô hình RandomForest
            RandomForest randomForest = new RandomForest();
            randomForest.buildClassifier(data);

            // Đánh giá mô hình sử dụng cross-validation
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(randomForest, data, 10, new java.util.Random(1));

            // Tính Mean Squared Error (MSE)
            double meanSquaredError = 0.0;
            for (int i = 0; i < data.numInstances(); i++) {
                double actualValue = data.instance(i).classValue();
                double predictedValue = eval.evaluateModelOnce(randomForest, data.instance(i));
                double squaredError = Math.pow(actualValue - predictedValue, 2);
                meanSquaredError += squaredError;
            }
            meanSquaredError /= data.numInstances();

            System.out.println("Mean Squared Error: " + meanSquaredError);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	@GetMapping("/getRatio/{thread}")
	public void getRatio(@PathVariable double thread) {
		int count=0;
		int countTrue=0;
		try {
            // Đọc dữ liệu từ tệp ARFF
			DataSource source = new DataSource("/Users/untiv/Desktop/NMKHDL/HousePriceBE/dataTraining.arff");
	        Instances data = source.getDataSet();
	        
	        // Thiết lập thuộc tính dự đoán (price) là thuộc tính cuối cùng trong dataset
	        data.setClassIndex(data.numAttributes() - 1);

	        // Tạo mô hình cây quyết định
	        RandomForest randomForest = new RandomForest();
	        randomForest.setNumFeatures(100); // Số lượng cây quyết định

	        // Huấn luyện mô hình
	        randomForest.buildClassifier(data);


            File f= new File("/Users/untiv/Desktop/NMKHDL/HousePriceBE/datatest.txt");
    		try {
    			BufferedReader file= Files.newBufferedReader(f.toPath(), StandardCharsets.UTF_8);	
    			while(true) {
    				String line= file.readLine();
    				if(line == null)
    					break;
    				
    				count++;
    				String []datas=line.split(",");
    				
    				
    				Instance newHouse = new DenseInstance(8); // 8 là số thuộc tính trong data training 

    				// Thiết lập giá trị cho các thuộc tính của căn nhà mới, ví dụ:
    				 
    		        newHouse.setValue(0,Integer.parseInt(datas[0])); 
    		        newHouse.setValue(1,Float.parseFloat(datas[1]) );
    		        newHouse.setValue(2,Integer.parseInt(datas[2]) );
    		        newHouse.setValue(3,Integer.parseInt(datas[3]) );
    		        newHouse.setValue(4,Integer.parseInt(datas[4]) );
    		        newHouse.setValue(5,Integer.parseInt(datas[5]) );
    		        newHouse.setValue(6,Integer.parseInt(datas[6]) );

    				newHouse.setMissing(7); // Giá sẽ được dự đoán, không cần thiết lập giá trị
    				
    				newHouse.setDataset(data);
    				 double predictedPrice1 = randomForest.classifyInstance(newHouse);
    				 if(Math.abs(Double.parseDouble(datas[7])-predictedPrice1)<thread)
    					 countTrue++;
    			}	
    			System.out.println("Tỉ lệ dự đoán đúng với ngưỡng "+thread+" là: "+(float)countTrue*100/(float)count);
    		} catch (Exception e) {
    			// TODO: handle exception
    		}
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
