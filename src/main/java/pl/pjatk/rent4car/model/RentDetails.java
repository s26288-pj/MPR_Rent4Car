package pl.pjatk.rent4car.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class RentDetails {
    private final UUID id;
    private final Car car;
    private final Client client;
    private final long totalPrice;
    private final LocalDate startDate;
    private final LocalDate endDate;
}
