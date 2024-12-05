package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(String transactionID, String accountID, double transactionAmount,
                          LocalDateTime transactionDate, String location, Channel channel) {

    private static final int FIELDS_COUNT = 6;
    private static final int TRANSACTION_ID_INDEX = 0;
    private static final int ACCOUNT_ID_INDEX = 1;
    private static final int TRANSACTION_AMOUNT_INDEX = 2;
    private static final int TRANSACTION_DATE_INDEX = 3;
    private static final int TRANSACTION_LOCATION_INDEX = 4;
    private static final int TRANSACTION_CHANNEL_INDEX = 5;

    public static Transaction of(String line) {
        String[] fields = line.split(",");

        if (fields.length != FIELDS_COUNT) {
            throw new IllegalArgumentException("Invalid transaction line: %s. " +
                    "Number of fields differ from the expected count of 5 " + line);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime dateTime = LocalDateTime.parse(fields[TRANSACTION_DATE_INDEX], formatter);
        return new Transaction(fields[TRANSACTION_ID_INDEX],
                fields[ACCOUNT_ID_INDEX],
                Double.parseDouble(fields[TRANSACTION_AMOUNT_INDEX]),
                dateTime,
                fields[TRANSACTION_LOCATION_INDEX],
                Enum.valueOf(Channel.class, fields[TRANSACTION_CHANNEL_INDEX].toUpperCase()));
    }
}
