import bg.sofia.uni.fmi.mjt.frauddetector.analyzer.TransactionAnalyzer;
import bg.sofia.uni.fmi.mjt.frauddetector.analyzer.TransactionAnalyzerImpl;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.FrequencyRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.LocationsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.SmallTransactionsRule;

import java.io.FileReader;
import java.io.Reader;
import java.time.Period;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        String filePath = "C:\\Users\\PC\\Desktop\\data.txt";

        try {
            Reader reader = new FileReader(filePath);
            List<Rule> rules = List.of(
                    new LocationsRule(3, 0.4),
                    new FrequencyRule(4, Period.ofWeeks(4), 0.25),
                    new SmallTransactionsRule(1, 10.20, 0.05)
            );

            TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, rules);

            System.out.println(analyzer.allAccountIDs());
            System.out.println(analyzer.allTransactionsByUser(analyzer.allTransactions().getFirst().accountID()));
            System.out.println(analyzer.accountsRisk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}