package bg.sofia.uni.fmi.mjt.goodreads.book.bookcomparator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b1;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b2;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b3;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecommendedBooksComparatorTest {

    private static RecommendedBooksComparator comparator;

    @BeforeAll
    static void setup() {
        comparator = new RecommendedBooksComparator(Map.of(
            b1, 1.0,
            b2, 2.0,
            b3, 1.0
        ));
    }

    @Test
    void testCompareShouldThrowWhenTheFirstGivenBookIsNull() {
        assertThrows(IllegalArgumentException.class, () -> comparator.compare(null, b2),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testCompareShouldThrowWhenTheSecondGivenBookIsNull() {
        assertThrows(IllegalArgumentException.class, () -> comparator.compare(b1, null),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testCompareShouldReturnOneWhenSecondBookRecommendedGradeIsGreater() {
        int result = comparator.compare(b1, b2);
        assertEquals(result, 1,
            "Expected compare to return 1, but returned " + result);
    }

    @Test
    void testCompareShouldReturnMinusOneWhenFirstBookRecommendedGradeIsGreater() {
        int result = comparator.compare(b2, b1);
        assertEquals(result, -1,
            "Expected compare to return -1, but returned " + result);
    }

    @Test
    void testCompareShouldReturnMinusOneWhenEqualRecommendationGradesButFirstBookIdIsGreater() {
        int result = comparator.compare(b3, b1);
        assertEquals(result, -1,
            "Expected compare to return -1, but returned " + result);
    }

    @Test
    void testCompareShouldReturnOneWhenEqualRecommendationGradesButSecondBookIdIsGreater() {
        int result = comparator.compare(b1, b3);
        assertEquals(result, 1,
            "Expected compare to return 1, but returned " + result);
    }

    @Test
    void testCompareShouldReturnZeroWhenGivenTheSameBooks() {
        int result = comparator.compare(b1, b1);
        assertEquals(result, 0,
            "Expected compare to return 0, but returned " + result);
    }


}