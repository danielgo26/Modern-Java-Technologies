package exception.status;

public class TooManyRequestException extends APIStatusException {

    public TooManyRequestException(String message) {
        super(message);
    }

    public TooManyRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
