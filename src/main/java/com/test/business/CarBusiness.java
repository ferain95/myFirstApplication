package com.test.business;

import com.test.Entity.Car;
import com.test.dto.CarDto;

import java.util.List;

public interface CarBusiness {

    CarDto insertCar(CarDto carDto);

    List<Car> carDetails();

    CarDto changeParam(CarDto carDto);

    void deleteCar(int id);

    Car dtoConvert(CarDto carDto);

    CarDto carConvert(Car car);

    void testTransactional(CarDto carDto);
}
