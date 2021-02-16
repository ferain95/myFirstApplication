package com.test.mapper;

import com.test.Entity.Car;
import com.test.dto.CarDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface CarMapper {

    void insertCar(Car car);

    List<Car> getAllCars();

    void deleteCar(@Param("id") int id);

    void changeParam(@Param("carDto") CarDto carDto);
}
