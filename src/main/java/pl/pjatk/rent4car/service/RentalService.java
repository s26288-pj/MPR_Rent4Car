package pl.pjatk.rent4car.service;

import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.stereotype.Service;
import pl.pjatk.rent4car.exception.DatabaseException;
import pl.pjatk.rent4car.exception.RentException;
import pl.pjatk.rent4car.exception.ValidationException;
import pl.pjatk.rent4car.model.Car;
import pl.pjatk.rent4car.model.Client;
import pl.pjatk.rent4car.repository.CarRepository;
import pl.pjatk.rent4car.repository.ClientRepository;

import java.util.Optional;

public class RentalService {
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;

    public RentalService(CarRepository carRepository, ClientRepository clientRepository) {
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
    }

    public void rentCar(Car car, Client client) {
        if(car.isRented()) {
            throw new RentException("Car is currently rented. Please look for another one.");
        }
        else if(client.getRentedCar() != null) {
            throw new RentException("Maximum 1 car per client.");
        }
        else {
            car.setRented(true);
            client.setRentedCar(car);
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
}
