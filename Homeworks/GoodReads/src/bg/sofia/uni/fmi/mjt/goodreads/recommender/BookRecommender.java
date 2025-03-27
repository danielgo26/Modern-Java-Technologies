package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.book.bookcomparator.RecommendedBooksComparator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateGreaterThan;
import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateNotNull;
import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateNotNullNorEmpty;

public class BookRecommender implements BookRecommenderAPI {

    private final Set<Book> books;
    private final SimilarityCalculator similarityCalculator;

    public BookRecommender(Set<Book> initialBooks, SimilarityCalculator calculator) {
        validateNotNullNorEmpty(initialBooks, "The given collection of book is null or empty");
        validateNotNull(calculator, "The given calculator is null");

        this.books = initialBooks;
        this.similarityCalculator = calculator;
    }

    @Override
    public SortedMap<Book, Double> recommendBooks(Book origin, int maxN) {
        validateNotNull(origin, "The given origin book is null");
        validateGreaterThan(maxN, 0, "The given maximum must be greater than zero");

        Map<Book, Double> bookRecommendations = getBookRecommendationsTo(origin);
        Comparator<Book> comparator = new RecommendedBooksComparator(bookRecommendations);

        SortedMap<Book, Double> sortedBooksByHigherRecommendation = new TreeMap<>(comparator);
        sortedBooksByHigherRecommendation.putAll(bookRecommendations);

        return sortedBooksByHigherRecommendation.entrySet()
            .stream()
            .limit(maxN)
            .collect(
                () -> new TreeMap<>(comparator),
                (tm, es) -> tm.put(es.getKey(), es.getValue()),
                TreeMap::putAll
            );
    }

    private Map<Book, Double> getBookRecommendationsTo(Book origin) {
        return this.books
            .stream()
            .filter(b -> !b.equals(origin))
            .collect(Collectors.toMap(
                b -> b,
                b -> this.similarityCalculator.calculateSimilarity(b, origin)
            ));
    }

}