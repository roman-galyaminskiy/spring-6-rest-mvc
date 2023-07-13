package guru.springframework.spring6restmvc.exceptions.handlers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ValidationErrorController {

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<Map<String, String>> handleJpaException(DataIntegrityViolationException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<List<Map<String, String>>> handleBindErrors(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errorList = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> Map.of(fieldError.getField(), fieldError.getDefaultMessage())).toList();
        return ResponseEntity.badRequest().body(errorList);
    }
}
