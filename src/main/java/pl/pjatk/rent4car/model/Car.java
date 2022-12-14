package pl.pjatk.rent4car.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Car {
    private final int id;
    private final String make;
    private final String model;
    private final String vin;
    private final CarClass carClass;
    private boolean isRented;
    private final int dailyRate;
}
