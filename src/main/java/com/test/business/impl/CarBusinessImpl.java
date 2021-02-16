package com.test.business.impl;

import com.test.Entity.Car;
import com.test.business.CarBusiness;
import com.test.dto.CarDto;
import com.test.mapper.CarMapper;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class CarBusinessImpl implements CarBusiness {

    @Autowired
    private CarControllerHelper carControllerHelper;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    CarTransactional carTransactional;

    @Override
    public CarDto insertCar(CarDto carDto) {
        Car car = dtoConvert(carDto);
        carMapper.insertCar(car);
        return carConvert(car);
    }

    public List<Car> carDetails() {
        return carMapper.getAllCars();
    }


    public void deleteCar(int id) {
        carMapper.deleteCar(id);
    }

    @Override
    public CarDto changeParam(CarDto carDto) {
        carMapper.changeParam(carDto);
        return carDto;
    }

    @Override
    public Car dtoConvert(CarDto carDto) {
        return carControllerHelper.convert(carDto);
    }

    @Override
    public CarDto carConvert(Car car) {
        return carControllerHelper.convert(car);
    }

    @Transactional
    public void testTransactional(CarDto carDto) {
        System.out.println("hello?!!!!!!!!!!!!!");
        Car car = dtoConvert(carDto);
        carMapper.insertCar(car);
//        try{
            carTransactional.testTransactional2(car);
//        }
//        catch (Exception e){
//            System.out.println("e");
//        }
        throw new RuntimeException();
//        System.out.println("after exception");
    }

}
