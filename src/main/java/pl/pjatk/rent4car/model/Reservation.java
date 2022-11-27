package pl.pjatk.rent4car.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Reservation {
    private final int clientId;
    private final int carId;
    private final LocalDate startDate;
    private final LocalDate endDate;
}
