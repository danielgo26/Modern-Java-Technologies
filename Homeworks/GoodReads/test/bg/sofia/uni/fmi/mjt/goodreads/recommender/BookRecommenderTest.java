package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite.CompositeSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions.TFIDFSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres.GenresOverlapSimilarityCalculator;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b1;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b2;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b3;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.basicBookSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookRecommenderTest {

    private static final Set<Book> books = basicBookSet;
    private static BookRecommender bookRecommender;

    @Test
    void testRecommendBooksShouldThrowWhenTheGivenBookIsNull() {
        SimilarityCalculator calculator = mock();
        bookRecommender = new BookRecommender(books, calculator);

        assertThrows(IllegalArgumentException.class,
            () -> bookRecommender.recommendBooks(null, 1),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testRecommendBooksShouldThrowWhenTheGivenMaxNIsLessOrEqToZero() {
        SimilarityCalculator calculator = mock();
        bookRecommender = new BookRecommender(books, calculator);

        assertThrows(IllegalArgumentException.class,
            () -> bookRecommender.recommendBooks(b1, 0),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testRecommendBooksShouldReturnValidSortedMapOfBooksBasedOnCompositeSimCalc() {
        CompositeSimilarityCalculator calculator = mock();
        when(calculator.calculateSimilarity(b2, b1)).thenReturn(0.4);
        when(calculator.calculateSimilarity(b3, b1)).thenReturn(0.7);

        bookRecommender = new BookRecommender(books, calculator);
        SortedMap<Book, Double> resulted = bookRecommender.recommendBooks(b1, 2);

        assertEquals(2, resulted.size(),
            "Expected to return map with size 2, but returned with size: " + resulted);
        assertEquals(resulted.firstEntry(), Map.entry(b3, 0.7),
            "Expected to the first entry to be: " + Map.entry(b3, 0.7)
                + ", but was: " + resulted.firstEntry());
        assertEquals(resulted.lastEntry(), Map.entry(b2, 0.4),
            "Expected to the last entry to be: " + Map.entry(b2, 0.4)
                + ", but was: " + resulted.lastEntry());
    }

    @Test
    void testRecommendBooksShouldReturnValidSortedMapOfBooksBasedOnGenresSimCalc() {
        GenresOverlapSimilarityCalculator calculator = mock();
        when(calculator.calculateSimilarity(b2, b1)).thenReturn(0.4);
        when(calculator.calculateSimilarity(b3, b1)).thenReturn(0.7);

        bookRecommender = new BookRecommender(books, calculator);
        SortedMap<Book, Double> resulted = bookRecommender.recommendBooks(b1, 2);

        assertEquals(2, resulted.size(),
            "Expected to return map with size 2, but returned with size: " + resulted);
        assertEquals(resulted.firstEntry(), Map.entry(b3, 0.7),
            "Expected to the first entry to be: " + Map.entry(b3, 0.7)
                + ", but was: " + resulted.firstEntry());
        assertEquals(resulted.lastEntry(), Map.entry(b2, 0.4),
            "Expected to the last entry to be: " + Map.entry(b2, 0.4)
                + ", but was: " + resulted.lastEntry());
    }

    @Test
    void testRecommendBooksShouldReturnValidSortedMapOfBooksBasedOnTfIdfSimCalc() {
        TFIDFSimilarityCalculator calculator = mock();
        when(calculator.calculateSimilarity(b2, b1)).thenReturn(0.4);
        when(calculator.calculateSimilarity(b3, b1)).thenReturn(0.7);

        bookRecommender = new BookRecommender(books, calculator);
        SortedMap<Book, Double> resulted = bookRecommender.recommendBooks(b1, 2);

        assertEquals(2, resulted.size(),
            "Expected to return map with size 2, but returned with size: " + resulted);
        assertEquals(resulted.firstEntry(), Map.entry(b3, 0.7),
            "Expected to the first entry to be: " + Map.entry(b3, 0.7)
                + ", but was: " + resulted.firstEntry());
        assertEquals(resulted.lastEntry(), Map.entry(b2, 0.4),
            "Expected to the last entry to be: " + Map.entry(b2, 0.4)
                + ", but was: " + resulted.lastEntry());
    }

}