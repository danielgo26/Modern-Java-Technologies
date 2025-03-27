package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FrequencyRuleTest {

    private static FrequencyRule frequencyRule;

    @Test
    void testFrequencyRuleConstructorShouldThrowWhenGivenNegativeThreshold() {
        assertThrows(IllegalArgumentException.class, () -> new FrequencyRule(-1, Period.ofWeeks(4), 0.25),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testFrequencyRuleConstructorShouldThrowWhenTheGivenTemporalAmountIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new FrequencyRule(1, null, 0.25),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testApplicableShouldThrowWhenTheGivenListOfTransactionsIsNull() {
        frequencyRule = new FrequencyRule(1, Period.ofWeeks(4), 0.25);
        assertThrows(IllegalArgumentException.class, () -> frequencyRule.applicable(null),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testApplicableShouldReturnTrueWhenGivenMoreFrequentTransactionsThanAllowed() {
        List<Transaction> transactions = getTransactionsWith(Period.ofMonths(1), 11);

        assertTrue(new FrequencyRule(10, Period.ofYears(1), 0.2).applicable(transactions),
            "Expected the rule to be applicable, because 11 transactions occurred in a period of 1 year, when the threshold is 10");
    }

    @Test
    void testApplicableShouldReturnTrueWhenGivenEqualFrequentTransactionsToAllowed() {
        List<Transaction> transactions = getTransactionsWith(Period.ofMonths(1), 10);

        assertTrue(new FrequencyRule(10, Period.ofYears(1), 0.2).applicable(transactions),
            "Expected the rule to be applicable, because 10 transactions occurred in a period of 1 year, when the threshold is 10");
    }

    @Test
    void testApplicableShouldReturnFalseWhenGivenLessFrequentTransactionsThanAllowed() {
        List<Transaction> transactions = getTransactionsWith(Period.ofMonths(1), 9);

        assertFalse(new FrequencyRule(10, Period.ofYears(1), 0.2).applicable(transactions),
            "Expected the rule not to be applicable, because 9 transactions occurred in a period of 1 year, when the threshold is 10");
    }

    private List<Transaction> getTransactionsWith(TemporalAmount dateDifference, int count) {
        List<Transaction> transactions = new ArrayList<>();

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime nextDate;

        for (int i = 0; i < count; i++) {
           nextDate = startDate.plus(dateDifference);
            transactions.add(new Transaction("trId", "accId", 1,
           nextDate, "location", Channel.ATM));
            startDate = nextDate;
        }

        return transactions;
    }

}