package exception.syntax;

public class APIRequestSyntaxException extends RuntimeException {

    public APIRequestSyntaxException(String message) {
        super(message);
    }

    public APIRequestSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

}