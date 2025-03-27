package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationsRuleTest {

    private static LocationsRule locationsRule;

    @Test
    void testLocationsRuleConstructorShouldThrowWhenGivenThresholdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new LocationsRule(-1, 1.0),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testApplicableShouldThrowWhenTheGivenListOfTransactionsIsNull() {
        locationsRule = new LocationsRule(1, 1.0);
        assertThrows(IllegalArgumentException.class, () -> locationsRule.applicable(null),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testApplicableShouldReturnTrueWhenTheGivenNumberOfDestinationsIsEqualThreshold() {
        int countDifferentTr = 5;
        int threshold = 5;
        locationsRule = new LocationsRule(threshold, 01.0);
        List<Transaction> transactions = getTransactionsWithDifferentLocations(countDifferentTr);

        assertTrue(locationsRule.applicable(transactions),
            String.format(
                "Expected the rule to be applicable, because %d different transactions given with threshold %d",
                countDifferentTr, threshold));
    }

    @Test
    void testApplicableShouldReturnTrueWhenTheGivenNumberOfDestinationsIsAboveThreshold() {
        int countDifferentTr = 6;
        int threshold = 5;
        locationsRule = new LocationsRule(threshold, 01.0);
        List<Transaction> transactions = getTransactionsWithDifferentLocations(countDifferentTr);

        assertTrue(locationsRule.applicable(transactions),
            String.format(
                "Expected the rule to be applicable, because %d different transactions given with threshold %d",
                countDifferentTr, threshold));
    }

    @Test
    void testApplicableShouldReturnFalseWhenTheGivenNumberOfDestinationsIsBelowThreshold() {
        int countDifferentTr = 4;
        int threshold = 5;
        locationsRule = new LocationsRule(threshold, 01.0);
        List<Transaction> transactions = getTransactionsWithDifferentLocations(countDifferentTr);

        assertFalse(locationsRule.applicable(transactions),
            String.format(
                "Expected the rule not to be applicable, because %d different transactions given with threshold %d",
                countDifferentTr, threshold));
    }

    private List<Transaction> getTransactionsWithDifferentLocations(int countDiffLocations) {
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < (countDiffLocations * 2) - 1; i++) {
            String locationAddition = (i % 2 == 0 ? "location" : "location" + i);
            transactions.add(new Transaction("trId", "accId", 1,
                LocalDateTime.now(), locationAddition, Channel.ATM));
        }

        return transactions;
    }

}