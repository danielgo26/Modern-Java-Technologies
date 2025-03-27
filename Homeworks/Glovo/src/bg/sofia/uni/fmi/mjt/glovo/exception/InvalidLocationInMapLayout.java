package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidLocationInMapLayout extends RuntimeException {
    public InvalidLocationInMapLayout(String message) {
        super(message);
    }

    public InvalidLocationInMapLayout(String message, Throwable cause) {
        super(message, cause);
    }
}
