package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.stream.Collectors;

public class FrequencyRule extends RuleImpl {

    private final int transactionCountThreshold;
    private final TemporalAmount timeWindow;

    public FrequencyRule(int transactionCountThreshold, TemporalAmount timeWindow, double weight) {
        super(weight);
        if (super.checkUnderLimit(transactionCountThreshold, 0)) {
            throw new IllegalArgumentException("Transaction cannot be a negative number");
        }
        if (timeWindow == null) {
            throw new IllegalArgumentException("Time window cannot be null");
        }

        this.transactionCountThreshold = transactionCountThreshold;
        this.timeWindow = timeWindow;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        super.verifyTransactionsNotNull(transactions);

        List<LocalDateTime> transactionsDates = transactions.stream()
                .map(Transaction::transactionDate)
                .sorted()
                .toList();

        for (LocalDateTime ldt : transactionsDates) {
            LocalDateTime startOfPeriodFirstWindow = ldt.minus(timeWindow);
            LocalDateTime endOfPeriodSecondWindow = ldt.plus(timeWindow);

            int countDatesWithinFirstTimeWindow = transactionsDates.stream()
                    .filter(e -> isDateWithInTimeWindow(e, startOfPeriodFirstWindow, ldt))
                    .collect(Collectors.collectingAndThen(Collectors.counting(), Long::intValue));

            int countDatesWithinSecondTimeWindow = transactionsDates.stream()
                    .filter(e -> isDateWithInTimeWindow(e, ldt, endOfPeriodSecondWindow))
                    .collect(Collectors.collectingAndThen(Collectors.counting(), Long::intValue));

            int maxCountDatesInWindow = Math.max(countDatesWithinFirstTimeWindow, countDatesWithinSecondTimeWindow);
            if (maxCountDatesInWindow >= this.transactionCountThreshold) {
                return true;
            }
        }

        return false;
    }

    private boolean isDateWithInTimeWindow(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        return date.isEqual(start) || date.isEqual(end) || (date.isAfter(start) && date.isBefore(end));
    }

}
