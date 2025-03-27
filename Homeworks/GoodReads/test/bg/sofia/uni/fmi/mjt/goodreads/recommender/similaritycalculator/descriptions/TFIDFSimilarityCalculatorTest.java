package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b1;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b2;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.basicBookSet;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.basicTextTokenizer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TFIDFSimilarityCalculatorTest {

    private static final TFIDFSimilarityCalculator calculator =
        new TFIDFSimilarityCalculator(basicBookSet, basicTextTokenizer);
    private static TFIDFSimilarityCalculator spyCalculator;

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
    void testCalculateSimilarityShouldReturnValidSimilarityCoefficientForTwoValidBooks() {
        spyCalculator = spy(calculator);
        when(spyCalculator.computeTFIDF(b1)).thenReturn(Map.of("a", 0.2, "b", 0.5));
        when(spyCalculator.computeTFIDF(b2)).thenReturn(Map.of("c", 0.4, "a", 0.7));

        double resulted = spyCalculator.calculateSimilarity(b1,b2);
        double expected = 0.3224574077934151;

        assertEquals(resulted, expected, 1e-6,
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

    @Test
    void testComputeTfIdfShouldThrowWhenTheGivenBookIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> calculator.computeTFIDF(null),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testComputeTfIdfShouldReturnValidMapOfTfIdfsWhenExistingBookInHashIsGiven() {
        spyCalculator = spy(calculator);
        when(spyCalculator.computeTF(b1)).thenReturn(Map.of("academy", 0.25, "club", 0.25, "superhero", 0.5));
        when(spyCalculator.computeIDF(b1)).thenReturn(Map.of("academy", 0.1, "club", 0.2, "superhero", 0.3));
        spyCalculator.computeTFIDF(b1);
        Map<String, Double> resulted = spyCalculator.computeTFIDF(b1);

        Map<String, Double> expected = Map.of(
            "superhero", 0.15,
            "club", 0.05,
            "academy", 0.025);

        assertEquals(resulted, expected,
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

    @Test
    void testComputeTfIdfShouldReturnValidMapOfTfIdfsWhenNonExistingBookInHashIsGiven() {
        spyCalculator = spy(calculator);
        when(spyCalculator.computeTF(b1)).thenReturn(Map.of("academy", 0.25, "club", 0.25, "superhero", 0.5));
        when(spyCalculator.computeIDF(b1)).thenReturn(Map.of("academy", 0.1, "club", 0.2, "superhero", 0.3));
        Map<String, Double> resulted = spyCalculator.computeTFIDF(b1);

        Map<String, Double> expected = Map.of(
            "superhero", 0.15,
            "club", 0.05,
            "academy", 0.025);

        assertEquals(resulted, expected,
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

    @Test
    void testComputeTfShouldThrowWhenTheGivenBookIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> calculator.computeTF(null),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testComputeTfShouldReturnValidMapOfTfsWhenValidBookIsGiven() {
        Map<String, Double> resulted = calculator.computeTF(b1);

        Map<String, Double> expected = Map.of(
            "superhero", 0.5,
            "academy", 0.25,
            "club", 0.25
        );

        assertEquals(resulted, expected,
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

    @Test
    void testComputeIdfShouldThrowWhenTheGivenBookIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> calculator.computeIDF(null),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testComputeIdfShouldReturnValidMapOfIdfsWhenValidBookIsGiven() {
        Map<String, Double> resulted = calculator.computeIDF(b1);

        Map<String, Double> expected = Map.of(
            "superhero", 0.17609125905568124,
            "academy", 0.47712125471966244,
            "club", 0.0
        );

        assertEquals(resulted, expected,
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

}