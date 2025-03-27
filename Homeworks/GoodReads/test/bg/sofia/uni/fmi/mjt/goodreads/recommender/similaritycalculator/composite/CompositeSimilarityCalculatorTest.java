package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b1;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompositeSimilarityCalculatorTest {

    private static SimilarityCalculator calculator;

    @BeforeAll
    static void setup() {
        SimilarityCalculator calc1 = mock();
        when(calc1.calculateSimilarity(b1, b2)).thenReturn(0.2);

        SimilarityCalculator calc2 = mock();
        when(calc2.calculateSimilarity(b1, b2)).thenReturn(0.5);

        calculator = new CompositeSimilarityCalculator(
            Map.of(
                calc1, 0.4,
                calc2, 0.6
            ));
    }

    @Test
    void testCalculateSimilarityShouldThrowWhenTheFirstGivenBookIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> calculator.calculateSimilarity(null, b2),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testCalculateSimilarityShouldThrowWhenTheSecondGivenBookIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> calculator.calculateSimilarity(b1, null),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testCalculateSimilarityShouldReturnValidCoefficientWhenValidBooksAreGiven() {
        double resulted = calculator.calculateSimilarity(b1, b2);
        double expected = 0.2 * 0.4 + 0.5 * 0.6;

        assertEquals(resulted, expected, 1e-6,
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

}