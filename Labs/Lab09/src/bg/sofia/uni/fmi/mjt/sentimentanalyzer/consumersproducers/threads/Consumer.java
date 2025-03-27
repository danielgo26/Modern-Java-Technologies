package bg.sofia.uni.fmi.mjt.sentimentanalyzer.consumersproducers.threads;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.ParallelSentimentAnalyzer;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentScore;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.consumersproducers.BlockingQueue;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.consumersproducers.Pair;

import java.util.Map;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.sentimentanalyzer.validator.InputValidator.validateNotNull;

public class Consumer extends Thread {

    private final BlockingQueue<Pair<String, String>> blockingQueue;
    private final Map<String, SentimentScore> textAnalyses;
    private final Set<String> stopWords;
    private final Map<String, SentimentScore> sentimentLexicon;
    private static Integer consumedTasks = 0;
    private static Integer entered = 0;
    private final Integer tasksCountToBeConsumed;

    public Consumer(BlockingQueue<Pair<String, String>> blockingQueue, Map<String, SentimentScore> textAnalyses,
                    ParallelSentimentAnalyzer analyzer) {
        validateNotNull(blockingQueue, "The given blocking queue is null!");
        validateNotNull(textAnalyses, "The given map of text analyses is null!");
        validateNotNull(analyzer, "The given analyzer is null!");

        this.blockingQueue = blockingQueue;
        this.textAnalyses = textAnalyses;
        this.stopWords = analyzer.getStopWords();
        this.sentimentLexicon = analyzer.getSentimentLexicon();
        tasksCountToBeConsumed = analyzer.getCurrentTasksCountToBeConsumed();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (Consumer.class) {
                if (consumedTasks + entered >= tasksCountToBeConsumed) {
                    break;
                }
                entered++;
            }

            Pair<String, String> current = this.blockingQueue.poll();

            synchronized (Consumer.class) {
                consumedTasks++;
                entered--;
            }

            String textId = current.first();
            String textToAnalyze = current.second();

            int totalScore = getScoreFromText(textToAnalyze);

            synchronized (textToAnalyze) {
                textAnalyses.put(textId, SentimentScore.fromScore(totalScore));
            }
        }
    }

    private int getScoreFromText(String textToAnalyze) {
        textToAnalyze = textToAnalyze.toLowerCase().replaceAll("[^a-z0-9'\\s]", "");

        for (String stopWord : stopWords) {
            stopWord = stopWord.toLowerCase();

            textToAnalyze = textToAnalyze.replaceAll(stopWord, "");
        }

        String[] words = textToAnalyze.split("\\s+");

        int totalScore = 0;
        for (String word : words) {
            SentimentScore score = sentimentLexicon.get(word);
            if (score != null) {
                totalScore += score.getScore();
            }
        }

        return totalScore;
    }

}