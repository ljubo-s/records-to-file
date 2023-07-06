package recordstofile.exception;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {

    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    ApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

}
