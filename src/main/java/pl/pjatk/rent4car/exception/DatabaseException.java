package pl.pjatk.rent4car.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DatabaseException extends RuntimeException {
    private final String messsage;
    private final Exception exception;
}
