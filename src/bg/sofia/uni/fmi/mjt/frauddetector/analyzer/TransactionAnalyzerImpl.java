package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {

    private final List<Transaction> transactions;
    private final List<Rule> rules;

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) {
        if (reader == null || rules == null) {
            throw new IllegalArgumentException("Invalid reader or rules - cannot be null");
        }
        double percentage = rules.stream()
                .mapToDouble(Rule::weight)
                .sum();

        if (Double.compare(percentage, 1.0) != 0) {
            throw new IllegalArgumentException("The rules coefficients sums is not 1");
        }

        try (var bufferedReader = new BufferedReader(reader)) {
            transactions = bufferedReader.lines().skip(1).map(Transaction::of).toList();
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot read from the given file reader", e);
        }
        this.rules = rules;
    }

    @Override
    public List<Transaction> allTransactions() {
        return transactions;
    }

    @Override
    public List<String> allAccountIDs() {
        return transactions.stream().map(Transaction::accountID).distinct().toList();
    }

    @Override
    public Map<Channel, Integer> transactionCountByChannel() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::channel,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
    }

    @Override
    public double amountSpentByUser(String accountID) {
        if (accountID == null || accountID.isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }

        return transactions.stream()
                .filter(transaction -> transaction.accountID().equals(accountID))
                .mapToDouble(Transaction::transactionAmount)
                .sum();
    }

    @Override
    public List<Transaction> allTransactionsByUser(String accountId) {
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("The given account ID cannot be null or empty");
        }

        return transactions.stream()
                .filter(transaction -> transaction.accountID().equals(accountId))
                .toList();
    }

    @Override
    public double accountRating(String accountId) {
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("The given account ID cannot be null or empty");
        }

        List<Transaction> accountsTransactions = this.allTransactionsByUser(accountId);

        double totalRisk = 0.0;
        for (Rule rule : rules) {
            double currentRuleRisk = (rule.applicable(accountsTransactions) ? rule.weight() : 0.0);
            totalRisk += currentRuleRisk;
        }

        return totalRisk;
    }

    @Override
    public SortedMap<String, Double> accountsRisk() {
        Map<String, Double> risksByAccounts = this.transactions.stream()
                .collect(Collectors.groupingBy(Transaction::accountID,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> accountRating(list.getFirst().accountID())
                        )));
        return new TreeMap<>(risksByAccounts);
    }

}
