package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZScoreRuleTest {

    private ZScoreRule zScoreRule;

    @Test
    void testZScoreRuleConstructorShouldThrowWhenGivenNegativeZScoreThreshold() {
        assertThrows(IllegalArgumentException.class,() -> new ZScoreRule(-1, 1.0),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testApplicableShouldReturnTrueWhenAZScoreThatIsAboveTheGivenThresholdExists() {
        zScoreRule = new ZScoreRule(0.0, 1.0);
        List<Transaction> transactions = getTransactions();

        assertTrue(zScoreRule.applicable(transactions),
            "Expected the rule to be applicable, because a transaction above the threshold exists");
    }

    @Test
    void testApplicableShouldReturnFalseWhenAZScoreThatIsAboveTheGivenThresholdDoesNotExist() {
        zScoreRule = new ZScoreRule(2, 1);
        List<Transaction> transactions = getTransactions();

        assertFalse(zScoreRule.applicable(transactions),
            "Expected the rule not to be applicable, because no transaction above the threshold exists");
    }

    private List<Transaction> getTransactions() {
        //The following transactions have similar coefficients of the listed measures to the ones given below:
        //m = 14 (middle value)
        //Var ~= 18,6 (Variance)
        //delta ~= 4,... (Standard deviation)
        //ZScores for each transaction:
        //  t1 : 10 -> ~-1
        //  t2 : 12 -> ~-0.5
        //  t3 : 20 -> ~1,5

        Transaction t1 = new Transaction("trId1", "accId", 10,
            LocalDateTime.now(), "location", Channel.ATM);
        Transaction t2 = new Transaction("trId2", "accId", 12,
            LocalDateTime.now(), "location", Channel.ATM);
        Transaction t3 = new Transaction("trId3", "accId", 20,
            LocalDateTime.now(), "location", Channel.ATM);

        return List.of(t1, t2, t3);
    }
}