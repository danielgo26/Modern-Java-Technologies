package bg.sofia.uni.fmi.mjt.sentimentanalyzer.validator;

public class InputValidator {

    public static <T extends Comparable<T>> void validateGreaterThan(T value, T from, String msg) {
        if (value.compareTo(from) <= 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void validateNotNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

}