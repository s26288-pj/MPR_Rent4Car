package pl.pjatk.rent4car.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.pjatk.rent4car.exception.RentException;
import pl.pjatk.rent4car.exception.ValidationException;
import pl.pjatk.rent4car.model.Car;
import pl.pjatk.rent4car.model.CarClass;
import pl.pjatk.rent4car.model.Client;
import pl.pjatk.rent4car.repository.CarRepository;
import pl.pjatk.rent4car.repository.ClientRepository;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RentalServiceTest {

    private static RentalService rentalService;
    private static CarRepository carRepository;
    private static ClientRepository clientRepository;

    @BeforeAll
    public static void setup() {
        carRepository = new CarRepository();
        clientRepository = new ClientRepository();
        rentalService = new RentalService(carRepository, clientRepository);
    }

    @AfterEach
    public void cleanUp() {
        carRepository.removeAll();
        clientRepository.removeAll();
    }

    @Test
    public void shouldAddNewCar() {
        Car car = new Car(
                1,
                "Peugeot",
                "308",
                "4325GH21SAW543GTE",
                CarClass.STANDARD,
                false
        );

        assertDoesNotThrow(() -> rentalService.addNewCar(car));
    }

    @ParameterizedTest
    @MethodSource("provideCarsWithEmptyVin")
    public void shouldThrowExceptionWhenAddingNewCarWithEmptyVin(Car car) {
        assertThrows(ValidationException.class, () -> rentalService.addNewCar(car), "VIN is required!");
    }

    @Test
    public void shouldFindCar() throws Exception {
        Car car = new Car(
                1,
                "Peugeot",
                "308",
                "4325GH21SAW543GTE",
                CarClass.STANDARD,
                false
        );

        carRepository.addCar(car);

        Optional<Car> foundCar = rentalService.findCarById(1);

        assertTrue(foundCar.isPresent());
        assertEquals(car, foundCar.get());

    }

    @Test
    public void shouldReturnEmptyOptionalWhenCarIsNotPresent() throws Exception {
        Optional<Car> foundCar = rentalService.findCarById(1);

        assertTrue(foundCar.isEmpty());
        assertEquals(Optional.empty(), foundCar);

    }

    private static Stream<Arguments> provideCarsWithEmptyVin() {
        return Stream.of(
                Arguments.of(new Car(1,"Peugeot","308","",CarClass.STANDARD,false
                )),
                Arguments.of(new Car(2,"Audi","A4",null,CarClass.STANDARD,false
                )),
                Arguments.of(new Car(3,"BMW","E36","   ",CarClass.PREMIUM,false))
        );
    }

    @Test
    public void shouldAddNewClient() {
        Client client = new Client(
                1,
                "Marek",
                "308",
                "Gdansk",
                null
        );

        assertDoesNotThrow(() -> rentalService.addNewClient(client));
    }

    @Test
    public void shouldBeAbleToRentACar() {
        Client client = new Client(1,"Marek","Zawisza","Banino",null);
        Car car = new Car(1,"Volvo","V13","GHDAS2350GSDF3",CarClass.STANDARD,false);

        assertDoesNotThrow(() -> rentalService.rentCar(car,client));
    }

    @Test
    public void shouldNotBeAbleToRentACar() {
        Client client = new Client(1,"Marek","Zawisza","Banino",null);
        Car car = new Car(1,"Volvo","V13","GHDAS2350GSDF3",CarClass.STANDARD,true);

        assertThrows(RentException.class,() -> rentalService.rentCar(car,client),"Could not rent a car.");
    }
}