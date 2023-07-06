package recordstofile.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    Logger logg = LogManager.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<Object> handleMyResourceNotFoundException(CustomException ex) {

        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex);
        logg.error("CustomException", ex);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {

        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex);
        logg.error("RuntimeException", ex);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = NullPointerException.class)
    protected ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {

        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex);
        logg.error("NullPointerException", ex);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {

        ApiError apiError = new ApiError(BAD_REQUEST, ex);
        apiError.setMessage("Input data not valid!");
        logg.error("MethodArgumentTypeMismatchException", ex);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {

        ApiError apiError = new ApiError(BAD_REQUEST, ex);
        apiError.setMessage("Input data constraint!");
        logg.error("ConstraintViolationException", ex);

        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
