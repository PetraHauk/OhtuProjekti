package utils;

public class ValidatorExeption extends RuntimeException {
    private final String errorKey;
    public ValidatorExeption(String message) {
        super(message);
        this.errorKey = message;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
