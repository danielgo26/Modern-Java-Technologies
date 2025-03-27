package bg.sofia.uni.fmi.mjt.olympics.comparator;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NationMedalComparatorTest {

    @Test
    void testConstructorShouldThrowWhenOlympicsObjectIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new NationMedalComparator(null),
            "Expected NationMedalComparator to throw, but nothing was thrown");
    }

    @Test
    void testCompareShouldReturnMinusOneWhenNationBHasLessMedalsThanNationA() {
       MJTOlympics olympicsMock = mock(MJTOlympics.class);
       NationMedalComparator comparator = new NationMedalComparator(olympicsMock);

        when(olympicsMock.getTotalMedals("A")).thenReturn(10);
        when(olympicsMock.getTotalMedals("B")).thenReturn(9);

        int result = comparator.compare("A", "B");
        assertEquals(result, -1,
            String.format("Expected to return -1 but actually returned %s", result));
    }

    @Test
    void testCompareShouldReturnPlusOneWhenNationBHasMoreMedalsThanNationA() {
        MJTOlympics olympicsMock = mock(MJTOlympics.class);
        NationMedalComparator comparator = new NationMedalComparator(olympicsMock);

        when(olympicsMock.getTotalMedals("A")).thenReturn(9);
        when(olympicsMock.getTotalMedals("B")).thenReturn(10);

        int result = comparator.compare("A", "B");
        assertEquals(result, 1,
            String.format("Expected to return 1 but actually returned %s", result));
    }

    @Test
    void testCompareShouldReturnMinusOneWhenNationAHasEqualMedalsCountAsNationB() {
        MJTOlympics olympicsMock = mock(MJTOlympics.class);
        NationMedalComparator comparator = new NationMedalComparator(olympicsMock);

        when(olympicsMock.getTotalMedals("A")).thenReturn(10);
        when(olympicsMock.getTotalMedals("B")).thenReturn(10);

        int result = comparator.compare("A", "B");
        assertEquals(result, -1,
            String.format("Expected to return -1 but actually returned %s", result));
    }

    @Test
    void testCompareShouldReturnPlusOneWhenNationBHasEqualMedalsCountAsNationA() {
        MJTOlympics olympicsMock = mock(MJTOlympics.class);
        NationMedalComparator comparator = new NationMedalComparator(olympicsMock);

        when(olympicsMock.getTotalMedals("A")).thenReturn(10);
        when(olympicsMock.getTotalMedals("B")).thenReturn(10);

        int result = comparator.compare("B", "A");
        assertEquals(result, 1,
            String.format("Expected to return 1 but actually returned %s", result));
    }

    @Test
    void testCompareShouldReturnZeroWhenNationAHasEqualMedalsCountAsNationA() {
        MJTOlympics olympicsMock = mock(MJTOlympics.class);
        NationMedalComparator comparator = new NationMedalComparator(olympicsMock);

        when(olympicsMock.getTotalMedals("A")).thenReturn(10);
        when(olympicsMock.getTotalMedals("B")).thenReturn(10);

        int result = comparator.compare("A", "A");
        assertEquals(result, 0,
            String.format("Expected to return 0 but actually returned %s", result));
    }

}