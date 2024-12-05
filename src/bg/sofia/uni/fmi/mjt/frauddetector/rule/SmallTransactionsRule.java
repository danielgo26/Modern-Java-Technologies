package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class SmallTransactionsRule extends RuleImpl {

    private final int countThreshold;
    private final double amountThreshold;

    public SmallTransactionsRule(int countThreshold, double amountThreshold, double weight) {
        super(weight);
        if (super.checkUnderLimit(countThreshold, 0)) {
            throw new IllegalArgumentException("Count of threshold cannot be a negative number");
        }
        if (super.checkUnderLimit(amountThreshold, 0.0)) {
            throw new IllegalArgumentException("Amount of threshold cannot be a negative number");
        }

        this.countThreshold = countThreshold;
        this.amountThreshold = amountThreshold;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        super.verifyTransactionsNotNull(transactions);

        return transactions.stream()
                .mapToDouble(Transaction::transactionAmount)
                .filter(e -> e <= this.amountThreshold)
                .count() >= this.countThreshold;
    }

}
