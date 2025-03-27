package bg.sofia.uni.fmi.mjt.goodreads.objectvalidator;

import java.util.Collection;
import java.util.Map;

public class ObjectValidator {

    public static void validateNotNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static <T> void validateNotNullNorEmpty(Collection<T> collection, String msg) {
        validateNotNull(collection, msg);
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static <K, V> void validateNotNullNorEmpty(Map<K, V> map, String msg) {
        validateNotNull(map, msg);
        if (map.isEmpty()) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static <T extends Comparable<? super T>> void validateGreaterThan(T value, T limit, String msg) {
        if (value.compareTo(limit) <= 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static <T extends Comparable<? super T>> void validateEqualTo(T value, T to, String msg) {
        if (!value.equals(to)) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static <T> void validateEachNotNull(Collection<T> elements) {
        int currentElCount = 0;
        for (T el : elements) {
            validateNotNull(el,
                String.format("Element with index: %d in the given collection is null", currentElCount));
            currentElCount++;
        }
    }

    public static <T> void validateEachNotNull(T[] elements) {
        for (int i = 0; i < elements.length; i++) {
            validateNotNull(elements[i],
                String.format("Element with index: %d in the given collection is null", i));
        }
    }

    public static <T, K> void validateEachNotNull(Map<T, K> map, String msg) {
        for (Map.Entry<T, K> entry : map.entrySet()) {
            validateNotNull(entry.getKey(), msg);
            validateNotNull(entry.getValue(), msg);
        }
    }

}