package com.test.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CarDto {
    private int id;
    private String brand;
    private String color;
    @NonNull private Integer fuelCapacity;
    private Integer seats;

}
