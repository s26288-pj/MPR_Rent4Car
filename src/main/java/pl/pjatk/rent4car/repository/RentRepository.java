package pl.pjatk.rent4car.repository;

import org.springframework.stereotype.Repository;
import pl.pjatk.rent4car.model.Car;
import pl.pjatk.rent4car.model.RentDetails;
import pl.pjatk.rent4car.model.Reservation;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RentRepository {
    private List<RentDetails> rentDetailsList = new ArrayList<>();

    public RentDetails save(RentDetails rentDetails) {
        rentDetailsList.add(rentDetails);

        return rentDetails;
    }

    public List<RentDetails> findAll() {
        return rentDetailsList;
    }
}
