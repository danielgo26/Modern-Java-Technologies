package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class ZScoreRule extends RuleImpl {

    private final double zScoreThreshold;

    public ZScoreRule(double zScoreThreshold, double weight) {
        super(weight);
        if (super.checkUnderLimit(zScoreThreshold, 0.0)) {
            throw new IllegalArgumentException("Z score threshold cannot be a negative number");
        }

        this.zScoreThreshold = zScoreThreshold;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        super.verifyTransactionsNotNull(transactions);

        double middleValue = transactions.stream()
                .mapToDouble(Transaction::transactionAmount)
                .sum() / transactions.size();

        double variance = transactions.stream()
                .mapToDouble(e -> Math.pow((e.transactionAmount() - middleValue), 2))
                .sum() / transactions.size();

        double standardDeviation = Math.sqrt(variance);

        for (Transaction transaction : transactions) {
            double zScore = (transaction.transactionAmount() - middleValue) / standardDeviation;
            if (zScore >= zScoreThreshold) {
                return true;
            }
        }

        return false;
    }
}
