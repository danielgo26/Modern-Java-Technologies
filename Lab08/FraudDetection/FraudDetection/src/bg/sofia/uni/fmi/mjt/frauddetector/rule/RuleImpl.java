package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public abstract class RuleImpl implements Rule {

    private final double weight;

    protected  <T extends Comparable<T>> boolean checkUnderLimit(T amount, T limit) {
        return amount.compareTo(limit) < 0;
    }

    protected  void verifyTransactionsNotNull(List<Transaction> transactions) {
        if (transactions == null) {
            throw new IllegalArgumentException("The collection of transactions cannot be null");
        }
    }

    public RuleImpl(double weight) {
        validateWeight(weight);
        this.weight = weight;
    }

    private void validateWeight(double weight) {
        if (weight < 0 || weight > 1) {
            throw new IllegalArgumentException("Weight must be between 0 and 1");
        }
    }

    @Override
    public abstract boolean applicable(List<Transaction> transactions);

    @Override
    public double weight() {
        return weight;
    }

}
