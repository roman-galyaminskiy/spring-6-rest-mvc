package guru.springframework.spring6restmvc.exceptions;

public class BeerNotFoundException extends RuntimeException {

    public BeerNotFoundException() {
        super();
    }

    public BeerNotFoundException(String message) {
        super(message);
    }

    public BeerNotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}
