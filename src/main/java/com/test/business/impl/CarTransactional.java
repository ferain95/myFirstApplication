package com.test.business.impl;

import com.test.Entity.Car;
import com.test.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CarTransactional {

    @Autowired
    CarMapper carMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @Transactional
//    @Transactional(propagation = Propagation.NESTED)
    public void testTransactional2(Car car) {
        car.setBrand("inner");
        carMapper.insertCar(car);
        throw new RuntimeException();
    }
}
