package bg.sofia.uni.fmi.mjt.sentimentanalyzer.consumersproducers.threads;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerInput;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.consumersproducers.BlockingQueue;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.consumersproducers.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import static bg.sofia.uni.fmi.mjt.sentimentanalyzer.validator.InputValidator.validateNotNull;

public class Producer extends Thread {

    private final BlockingQueue<Pair<String, String>> blockingQueue;
    private final AnalyzerInput analyzerInput;

    public Producer(BlockingQueue<Pair<String, String>> blockingQueue, AnalyzerInput analyzerInput) {
        validateNotNull(blockingQueue, "The given blocking queue is null!");
        validateNotNull(analyzerInput, "The given analyzer input is null!");

        this.blockingQueue = blockingQueue;
        this.analyzerInput = analyzerInput;
    }

    @Override
    public void run() {
        String textId = this.analyzerInput.inputID();
        Reader reader = this.analyzerInput.inputReader();
        StringBuilder content = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            boolean firstLine = true;

            while ((line = bufferedReader.readLine()) != null) {
                if (firstLine) {
                    content.append(line);
                    firstLine = false;
                } else {
                    content.append("\n").append(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot read data from the given reader!", e);
        }

        this.blockingQueue.add(new Pair<>(textId, content.toString()));
    }
}