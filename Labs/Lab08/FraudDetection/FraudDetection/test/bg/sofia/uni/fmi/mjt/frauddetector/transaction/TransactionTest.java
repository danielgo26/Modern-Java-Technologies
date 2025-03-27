package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionTest {

    @Test
    void testTransactionOfThrowWhenGivenLineWithLessFields() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.of(""),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testTransactionOfShouldReturnValidTransactionObjectFromString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Transaction expected = new Transaction("TX000001", "AC00128", 14.09,
            LocalDateTime.parse("2023-04-11 16:29:14", formatter), "San Diego", Channel.ATM);

        String inputLine = "TX000001,AC00128,14.09,2023-04-11 16:29:14,San Diego,ATM";

        Transaction returned = Transaction.of(inputLine);

        assertEquals(expected, returned,
            String.format("Expected to return %s, actually returned %s", expected, returned));
    }

}