package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmallTransactionsRuleTest {

    private static SmallTransactionsRule smallTransactionsRule;

    @Test
    void testSmallTransactionsRuleConstructorShouldThrowWhenCountThresholdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new SmallTransactionsRule(-1, 1, 1.0),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testSmallTransactionsRuleConstructorShouldThrowWhenAmountThresholdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new SmallTransactionsRule(1, -1, 1.0),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testApplicableShouldThrowWhenTheGivenListOfTransactionsIsNull() {
        smallTransactionsRule = new SmallTransactionsRule(1, 1, 1.0);
        assertThrows(IllegalArgumentException.class, () -> smallTransactionsRule.applicable(null),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testApplicableShouldReturnTrueWhenTheCountOfSmallTransactionsIsAboveThreshold() {
        int countThreshold = 10;
        double amountThreshold = 10.0;
        int countTransactionsAbove = 11;
        smallTransactionsRule = new SmallTransactionsRule(countThreshold, amountThreshold, 1.0);

        List<Transaction> transactions = getTransactionsWithAmountLessThan(countTransactionsAbove, amountThreshold);

        assertTrue(smallTransactionsRule.applicable(transactions),
            String.format(
                "Expected the rule to be applicable, because %d transactions with amount less than %f are above the given threshold: %d",
                countTransactionsAbove, amountThreshold, countThreshold));
    }

    @Test
    void testApplicableShouldReturnTrueWhenTheCountOfSmallTransactionsIsEqualToTheThreshold() {
        int countThreshold = 10;
        double amountThreshold = 10.0;
        int countTransactionsAbove = 10;
        smallTransactionsRule = new SmallTransactionsRule(countThreshold, amountThreshold, 1.0);

        List<Transaction> transactions = getTransactionsWithAmountLessThan(countTransactionsAbove, amountThreshold);

        assertTrue(smallTransactionsRule.applicable(transactions),
            String.format(
                "Expected the rule to be applicable, because %d transactions with amount less than %f are above the given threshold: %d",
                countTransactionsAbove, amountThreshold, countThreshold));
    }

    @Test
    void testApplicableShouldReturnFalseWhenTheCountOfSmallTransactionsIsLessThanTheThreshold() {
        int countThreshold = 10;
        double amountThreshold = 10.0;
        int countTransactionsAbove = 9;
        smallTransactionsRule = new SmallTransactionsRule(countThreshold, amountThreshold, 1.0);

        List<Transaction> transactions = getTransactionsWithAmountLessThan(countTransactionsAbove, amountThreshold);

        assertFalse(smallTransactionsRule.applicable(transactions),
            String.format(
                "Expected the rule not to be applicable, because total count of %d transactions are less than the given threshold: %d",
                countTransactionsAbove, countThreshold));
    }

    private List<Transaction> getTransactionsWithAmountLessThan(int countTransactions, double amount) {
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < (countTransactions * 2); i++) {
            double currAmount = (i % 2 == 0 ? (amount - i) % amount : amount + i);
            transactions.add(new Transaction("trId", "accId", currAmount,
                LocalDateTime.now(), "location", Channel.ATM));
        }

        return transactions;
    }

}