package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class LocationsRule extends RuleImpl {

    private final int threshold;

    public LocationsRule(int threshold, double weight) {
        super(weight);
        if (super.checkUnderLimit(threshold, 0)) {
            throw new IllegalArgumentException("Threshold cannot be a negative number");
        }
        this.threshold = threshold;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        super.verifyTransactionsNotNull(transactions);

        return transactions.stream()
                .map(Transaction::location)
                .distinct().count() >= this.threshold;
    }

}
