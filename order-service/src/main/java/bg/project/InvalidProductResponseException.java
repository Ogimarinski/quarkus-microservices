package bg.project;

public class InvalidProductResponseException extends RuntimeException {
    public InvalidProductResponseException(String message) {
        super(message);
    }
}
