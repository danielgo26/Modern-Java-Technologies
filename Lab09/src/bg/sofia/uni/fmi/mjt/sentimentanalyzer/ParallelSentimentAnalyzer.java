package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.consumersproducers.BlockingQueue;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.consumersproducers.Pair;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.consumersproducers.threads.Consumer;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.consumersproducers.threads.Producer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.sentimentanalyzer.validator.InputValidator.validateGreaterThan;
import static bg.sofia.uni.fmi.mjt.sentimentanalyzer.validator.InputValidator.validateNotNull;

public class ParallelSentimentAnalyzer implements SentimentAnalyzerAPI {

    private final Integer workersCount;
    private final Set<String> stopWords;
    private final Map<String, SentimentScore> sentimentLexicon;
    private Integer currentTasksCountToBeConsumed = 0;

    /**
     * @param workersCount     number of consumer workers
     * @param stopWords        set containing stop words
     * @param sentimentLexicon map containing the sentiment lexicon,
     *                         where the key is the word and the value is the sentiment score
     */
    public ParallelSentimentAnalyzer(int workersCount, Set<String> stopWords,
                                     Map<String, SentimentScore> sentimentLexicon) {
        validateGreaterThan(workersCount, 0, "The given workers count must be greater than zero!");
        validateNotNull(stopWords, "The given stop words collection is null!");
        validateNotNull(sentimentLexicon, "The given sentiment lexicon is null!");

        this.workersCount = workersCount;
        this.stopWords = stopWords;
        this.sentimentLexicon = sentimentLexicon;
    }

    @Override
    public Map<String, SentimentScore> analyze(AnalyzerInput... input) {
        Map<String, SentimentScore> textAnalyses = new HashMap<>();
        BlockingQueue<Pair<String, String>> blockingQueue = new BlockingQueue<>(this.workersCount);
        currentTasksCountToBeConsumed = input.length;
        Producer[] producers = new Producer[input.length];
        Consumer[] consumers = new Consumer[this.workersCount];
        for (int i = 0; i < input.length; i++) {
            producers[i] = new Producer(blockingQueue, input[i]);
            producers[i].start();
        }
        for (int i = 0; i < this.workersCount; i++) {
            consumers[i] = new Consumer(blockingQueue, textAnalyses, this);
            consumers[i].start();
        }
        for (int i = 0; i < input.length; i++) {
            try {
                producers[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(String.format("Thread %s could not be joined", producers[i].getName()), e);
            }
        }
        for (int i = 0; i < this.workersCount; i++) {
            try {
                consumers[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(String.format("Thread %s could not be joined", consumers[i].getName()), e);
            }
        }
        return textAnalyses;
    }

    public Set<String> getStopWords() {
        return this.stopWords;
    }

    public Map<String, SentimentScore> getSentimentLexicon() {
        return this.sentimentLexicon;
    }

    public synchronized Integer getCurrentTasksCountToBeConsumed() {
        return currentTasksCountToBeConsumed;
    }

}