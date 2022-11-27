package pl.pjatk.rent4car.controler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pjatk.rent4car.exception.ValidationException;
import pl.pjatk.rent4car.model.Car;
import pl.pjatk.rent4car.model.Client;
import pl.pjatk.rent4car.model.RentDetails;
import pl.pjatk.rent4car.model.Reservation;
import pl.pjatk.rent4car.service.RentalService;

import java.util.List;

@RestController
@RequestMapping("/rent")
public class RentalControler {
    private RentalService rentalService;

    public RentalControler(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("/addclient")
    public ResponseEntity<Client> saveClient(@RequestBody Client client) {
        try {
            rentalService.addNewClient(client);
        }
        catch (ValidationException validationException) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(client);
    }

    @PostMapping("/addcar")
    public ResponseEntity<Car> saveCar(@RequestBody Car car) {
        try {
            rentalService.addNewCar(car);
        }
        catch (ValidationException validationException) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(car);
    }

    @PostMapping("/reserve")
    public ResponseEntity createNewReservation(@RequestBody Reservation reservation) {
        try {
            rentalService.rentCar(reservation);
        }
        catch (ValidationException validationException) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(reservation);
    }

    @GetMapping("getreservations")
    public ResponseEntity<List<RentDetails>> getAllReservations() {
        List<RentDetails> allReservations = rentalService.findAllReservations();

        return ResponseEntity.ok(allReservations);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Car>> getAllCars(@RequestParam(required=false, name="isRented") Boolean isRented) {
        List<Car> allCars = rentalService.findAllCars(isRented);

        return ResponseEntity.ok(allCars);
    }
}
