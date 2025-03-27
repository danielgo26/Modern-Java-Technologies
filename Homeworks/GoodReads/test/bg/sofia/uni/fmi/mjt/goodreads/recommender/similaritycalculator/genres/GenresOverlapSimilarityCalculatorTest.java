package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b1;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenresOverlapSimilarityCalculatorTest {

    private static final SimilarityCalculator similarityCalculator =
        new GenresOverlapSimilarityCalculator();

    @Test
    void testCalculateSimilarityShouldThrowWhenTheFirstGivenBookIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> similarityCalculator.calculateSimilarity(null, b2),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testCalculateSimilarityShouldThrowWhenTheSecondGivenBookIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> similarityCalculator.calculateSimilarity(b1, null),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testCalculateSimilarityShouldReturnZeroWhenGivenBookWithNoGenres() {
        Book b1 = new Book("1", "To Kill a Mockingbird", "Harper Lee",
            "A novel about the serious issues of rape and racial inequality.",
            List.of("Fiction", "Classic", "Historical"), 4.8, 250000, "https://example.com/tokillamockingbird");
        Book b2 = new Book("2", "1984", "George Orwell",
            "A dystopian novel set in a totalitarian society ruled by Big Brother.",
            List.of(), 4.7, 320000, "https://example.com/1984");

        double resulted = similarityCalculator.calculateSimilarity(b1, b2);

        double expected = 0.0;

        assertEquals(resulted, expected, 1e-6,
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

    @Test
    void testCalculateSimilarityShouldReturnValidCoefficientForTwoValidBooks() {
        double resulted = similarityCalculator.calculateSimilarity(b1, b2);

        double expected = 1.0 / 3.0;

        assertEquals(resulted, expected, 1e-6,
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

}