package bg.sofia.uni.fmi.mjt.goodreads.finder;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum MatchOption {
    MATCH_ALL,
    MATCH_ANY;

    public static <T> BiPredicate<Stream<T>, Predicate<T>> getMatchFunction(MatchOption matchOption) {
        return switch (matchOption) {
            case MATCH_ALL -> Stream::allMatch;
            case MATCH_ANY -> Stream::anyMatch;
            default -> throw new UnsupportedOperationException("The given match option does not exist");
        };
    }
}