package pl.pjatk.rent4car.service;

import org.springframework.stereotype.Service;
import pl.pjatk.rent4car.exception.DatabaseException;
import pl.pjatk.rent4car.exception.RentException;
import pl.pjatk.rent4car.exception.ValidationException;
import pl.pjatk.rent4car.model.Car;
import pl.pjatk.rent4car.model.Client;
import pl.pjatk.rent4car.model.RentDetails;
import pl.pjatk.rent4car.model.Reservation;
import pl.pjatk.rent4car.repository.CarRepository;
import pl.pjatk.rent4car.repository.ClientRepository;
import pl.pjatk.rent4car.repository.RentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class RentalService {
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;
    private final RentRepository rentRepository;

    public RentalService(CarRepository carRepository, ClientRepository clientRepository, RentRepository rentRepository) {
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
        this.rentRepository = rentRepository;
    }

    public void rentCar(Reservation reservation) {
        Optional<Client> client = clientRepository.findClientById(reservation.getClientId());
        Optional<Car> car = carRepository.findCarById(reservation.getCarId());

        if(car.isPresent()) {
            Client existingClient = client.get();
            Car existingCar = car.get();
            if(existingCar.isRented()) {
                throw new RentException("Car is currently rented. Please look for another one.");
            }
            else if(existingClient.getRentedCar() != null) {
                throw new RentException("Maximum 1 car per client.");
            }
            else {
                long totalPrice = existingCar.getDailyRate() * DAYS.between(reservation.getEndDate(), reservation.getStartDate());

                RentDetails rentDetails = new RentDetails(
                        UUID.randomUUID(),
                        existingCar,
                        existingClient,
                        totalPrice,
                        reservation.getStartDate(),
                        reservation.getEndDate()
                );
                existingCar.setRented(true);
                existingClient.setRentedCar(existingCar);
                rentRepository.save(rentDetails);
            }
        }
        else {
            throw new ValidationException("Car doesn't exist!");
        }
    }

    public void addNewClient(Client client) {
        if (isValid(client.getName())) {
            throw new ValidationException("Name is required!");
        }
        if (isValid(client.getSurname())) {
            throw new ValidationException("Surname is required!");
        }
        if (isValid(client.getCity())) {
            throw new ValidationException("City is required!");
        }

        try {
            clientRepository.addClient(client);
        } catch (Exception e) {
            throw new DatabaseException("Database error: ", e);
        }
    }

    public void addNewCar(Car car) {
        if (isValid(car.getVin())) {
            throw new ValidationException("VIN is required!");
        }
        if (isValid(car.getMake())) {
            throw new ValidationException("Make is required!");
        }

        try {
            carRepository.addCar(car);
        } catch (Exception e) {
            throw new DatabaseException("Database error: ", e);
        }
    }

    public Optional<Car> findCarById(int id) {
        return carRepository.findCarById(id);
    }

    private static boolean isValid(String attribute) {
        return attribute == null || attribute.isBlank();
    }

    public List<Car> findAllCars(Boolean isRented) {
        List<Car> carList = carRepository.findAll();

        if(isRented != null) {
            return carList.stream().filter(it -> isRented.equals(it.isRented())).collect(Collectors.toList());
        }
        else {
            return carList;
        }
    }

    public List<RentDetails> findAllReservations() {
        List<RentDetails> reservationList = rentRepository.findAll();

        return reservationList;
    }
}
