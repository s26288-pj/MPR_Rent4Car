package pl.pjatk.rent4car.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RentException extends RuntimeException {
        private final String messsage;
}
