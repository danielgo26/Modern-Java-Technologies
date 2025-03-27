package bg.sofia.uni.fmi.mjt.goodreads.book;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookTest {

    @Test
    void testBookOfShouldThrowWhenTheGivenTokensArrayIsNull() {
        assertThrows(IllegalArgumentException.class, () -> Book.of(null),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testBookOfShouldThrowWhenTheGivenTokensArrayHasInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> Book.of(new String[] {""}),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testBookOfShouldThrowWhenTheGivenTokensArrayContainsNullElement() {
        assertThrows(IllegalArgumentException.class,
            () -> Book.of(new String[] {"", "", "", "", "['Comedy', null]", null, "1", ""}),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testBookOfShouldReturnValidBook() {
        assertDoesNotThrow(() -> Book.of(new String[] {"", "", "", "", "['Comedy', 'Drama']", "1.0", "1", ""}),
            "Expected nothing to be thrown");
    }

}