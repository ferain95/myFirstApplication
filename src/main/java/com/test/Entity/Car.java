package com.test.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
public class Car {


    Integer id;
    String brand;
    String color;
    Integer fuelCapacity;
    Integer seats;
}
