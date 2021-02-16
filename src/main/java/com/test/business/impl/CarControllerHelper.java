package com.test.business.impl;

import cn.hutool.core.bean.BeanUtil;
import com.test.Entity.Car;
import com.test.dto.CarDto;
import org.springframework.stereotype.Component;

@Component
public class CarControllerHelper {

    public Car convert (CarDto carDto) {
        Car car = new Car();
        BeanUtil.copyProperties(carDto, car);

        return car;
    }

    public CarDto convert (Car car) {
        CarDto carDto = new CarDto();
        BeanUtil.copyProperties(car, carDto);

        return carDto;
    }


}
