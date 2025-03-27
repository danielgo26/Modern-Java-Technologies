package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.FrequencyRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.LocationsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.SmallTransactionsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionAnalyzerImplTest {

    private static String lines = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel\n" +
        "TX000001,AC00128,14.09,2023-04-11 16:29:14,San Diego,ATM\n" +
        "TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,Branch\n" +
        "TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online\n";

    private static Reader reader;
    private static List<Rule> rules;
    private static List<Transaction> transactions;
    private static TransactionAnalyzer analyzer;
    private static Transaction t1;
    private static Transaction t2;
    private static Transaction t3;

    @BeforeAll
    static void setup() {
        rules = new ArrayList<>();
        rules.add(new LocationsRule(1, 1));
        transactions = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        t1 = new Transaction("TX000001", "AC00128", 14.09,
            LocalDateTime.parse("2023-04-11 16:29:14", formatter), "San Diego", Channel.ATM);
        t2 = new Transaction("TX000002", "AC00455", 376.24,
            LocalDateTime.parse("2023-06-27 16:44:19", formatter), "Houston", Channel.BRANCH);
        t3 = new Transaction("TX000003", "AC00019", 126.29,
            LocalDateTime.parse("2023-07-10 18:16:08", formatter), "Mesa", Channel.ONLINE);

        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);
    }

    @Test
    void testConstructorShouldThrowWhenGivenANullArgument() {
        assertThrows(IllegalArgumentException.class, () -> new TransactionAnalyzerImpl(null, null),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testConstructorShouldThrowWhenGivenRulesWithWeightsMoreThan1() {
        reader = mock();
        assertThrows(IllegalArgumentException.class, () -> new TransactionAnalyzerImpl(reader, new ArrayList<>()),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testAllTransactionsShouldReturnValidCollectionOfTransactions() {
        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(new StringReader(lines), rules);
        List<Transaction> resultTransactions = analyzer.allTransactions();
        assertIterableEquals(transactions, resultTransactions,
            String.format("Expected to return %s, but actually returned %s", transactions, resultTransactions));
    }

    @Test
    void testAllAccountsIDsShouldReturnValidCollectionOfAccountsIDs() {
        List<String> ids = new ArrayList<>(Arrays.asList("AC00128", "AC00455", "AC00019"));
        List<String> returned = getDefaultAnalyzer().allAccountIDs();

        assertIterableEquals(ids, getDefaultAnalyzer().allAccountIDs(),
            String.format("Expected to return %s, but actually returned %s", ids, returned));
    }

    private TransactionAnalyzer getDefaultAnalyzer() {
        return new TransactionAnalyzerImpl(new StringReader(lines), rules);
    }

    @Test
    void testTransactionCountByChannelShouldReturnAValidMapFromChanelAndCount() {
        Map<Channel, Integer> transactionCountPerChannel = new HashMap<>();
        transactionCountPerChannel.put(Channel.ATM, 1);
        transactionCountPerChannel.put(Channel.BRANCH, 1);
        transactionCountPerChannel.put(Channel.ONLINE, 1);

        Map<Channel, Integer> returned = getDefaultAnalyzer().transactionCountByChannel();

        assertEquals(transactionCountPerChannel,
            getDefaultAnalyzer().transactionCountByChannel(),
            String.format("Expected to return: %s, but actually returned: %s",
                transactionCountPerChannel, returned));
    }

    @Test
    void testAmountSpentByUserShouldThrowWhenAccountIDIsNull() {
        assertThrows(IllegalArgumentException.class, () -> getDefaultAnalyzer().amountSpentByUser(null),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testAmountSpendByUserShouldReturnTheActualAmountSpentByUser() {
        double returnedAmount = getDefaultAnalyzer().amountSpentByUser("AC00128");
        assertEquals(14.09, returnedAmount,
            String.format("Expected to return 14.09, but actually returned %f", returnedAmount));
    }

    @Test
    void testAllTransactionsByUserShouldThrowWhenAccountIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> getDefaultAnalyzer().allTransactionsByUser(null),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testAllTransactionsByUserShouldReturnValidCollectionOfTheUsersTransactions() {
        List<Transaction> returned = getDefaultAnalyzer().allTransactionsByUser("AC00128");
        List<Transaction> expected = new ArrayList<>(List.of(t1));

        assertIterableEquals(expected, returned,
            String.format("Expected to return %s, but actually returned %s", expected, returned));
    }

    @Test
    void testAccountRatingShouldThrowWhenAccountIDIsNull() {
        assertThrows(IllegalArgumentException.class, () -> getDefaultAnalyzer().accountRating(null),
            "Expected IllegalArgumentException to be thrown!");
    }

    @Test
    void testAccountRatingShouldReturnValidRatingForOnlyLocationsRuleApplicable() {
        LocationsRule locationsRule = mock();
        when(locationsRule.weight()).thenReturn(1.0);
        when(locationsRule.applicable(any())).thenReturn(true);
        analyzer = new TransactionAnalyzerImpl(new StringReader(lines), new ArrayList<>(List.of(locationsRule)));

        double returned = analyzer.accountRating("AC00128");

        assertEquals(returned, 1.0);
    }

    @Test
    void testAccountsRiskShouldReturnValidSortedMapFromAccountsWithAccordingRisks() {
        LocationsRule locationsRule = mock();
        when(locationsRule.weight()).thenReturn(0.3);
        when(locationsRule.applicable(List.of(t1))).thenReturn(true);
        when(locationsRule.applicable(List.of(t2))).thenReturn(true);
        when(locationsRule.applicable(List.of(t3))).thenReturn(false);

        SmallTransactionsRule smallTransactionsRule = mock();
        when(smallTransactionsRule.weight()).thenReturn(0.5);
        when(smallTransactionsRule.applicable(List.of(t1))).thenReturn(true);
        when(smallTransactionsRule.applicable(List.of(t2))).thenReturn(false);
        when(smallTransactionsRule.applicable(List.of(t3))).thenReturn(true);

        FrequencyRule frequencyRule = mock();
        when(frequencyRule.weight()).thenReturn(0.2);
        when(frequencyRule.applicable(List.of(t1))).thenReturn(false);
        when(frequencyRule.applicable(List.of(t2))).thenReturn(true);
        when(frequencyRule.applicable(List.of(t3))).thenReturn(true);

        analyzer = new TransactionAnalyzerImpl(new StringReader(lines),
            new ArrayList<>(Arrays.asList(locationsRule, smallTransactionsRule, frequencyRule)));

        Map<String, Double> expected = new TreeMap<>();
        expected.put("AC00128", 0.8);
        expected.put("AC00455", 0.5);
        expected.put("AC00019", 0.7);


        Map<String, Double> returned = analyzer.accountsRisk();

        assertEquals(expected, returned,
            String.format("Expected to return %s, but actually returned %s", expected, returned));
    }

}