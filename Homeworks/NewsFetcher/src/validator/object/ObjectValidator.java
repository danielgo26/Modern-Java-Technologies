package validator.object;

import exception.syntax.APIRequestSyntaxException;

import java.util.Collection;

public class ObjectValidator {

    private ObjectValidator() { }

    public static void validateNotNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static <T extends Comparable<T>> void validateCollection(Collection<T> collection, String collectionName) {
        validateNotNullNorEmpty(collection,
            String.format("The given %s collection cannot be null or empty!", collectionName));
        validateEachNotNullNorEmpty(collection,
            String.format("The given %s collection contains null or empty elements!", collectionName));
    }

    public static void validateRequestEntityWithinInterval(int count, int min, int max, String msg) {
        if (count < min || count > max) {
            throw new APIRequestSyntaxException(msg);
        }
    }

    public static void validateRequestEntityGreaterThan(int count, int min, String msg) {
        if (count < min ) {
            throw new APIRequestSyntaxException(msg);
        }
    }

    private static void validateNotNullNorEmpty(Collection<?> collection, String msg) {
        validateNotNull(collection, "The given collection cannot be null!");
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(msg);
        }
    }

    private static <T extends Comparable<T>> void validateEachNotNullNorEmpty(Collection<T> iterable, String msg) {
        for (T el : iterable) {
            validateNotNull(el, msg);
            if (el.equals("")) {
                throw new IllegalArgumentException(msg);
            }
        }
    }

}