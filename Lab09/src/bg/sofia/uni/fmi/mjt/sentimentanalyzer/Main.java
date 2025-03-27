package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        Set<String> stopWords = Set.of(
            "sometimes",
            "work",
            "I"
        );

        Map<String, SentimentScore> lexicon =
            Map.of(
                "love", SentimentScore.fromScore(3),
                "hate", SentimentScore.fromScore(-3),
                "happy", SentimentScore.fromScore(2),
                "sad", SentimentScore.fromScore(-2),
                "good", SentimentScore.fromScore(1),
                "fun", SentimentScore.fromScore(2),
                "bad", SentimentScore.fromScore(-1)
            );

        ParallelSentimentAnalyzer analyzer = new ParallelSentimentAnalyzer(2, stopWords, lexicon);

        String[] inputs = {
            "I love programming, it's so fun. But sometimes I hate when the code doesn't work.",
            "Today was a good day. I felt happy and accomplished, though I had some challenges.",
            "I feel so sad today. Everything seems bad and nothing is going right.",
            "I love working on new projects. However, I hate the pressure of deadlines.",
            "Life is good. I am happy with my work and personal life.",
            "The weather is nice today, which makes me feel good. I love sunny days.",
            "I feel bad about the mistakes I made yesterday. It's tough to fix things.",
            "Hate is such a strong word. It's better to focus on good things.",
            "Good things come to those who wait. I am confident about the future.",
            "Sad to see my friends leave, but I know they will be successful in their new journey."
        };

        AnalyzerInput[] analyzerInputs = new AnalyzerInput[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            analyzerInputs[i] = new AnalyzerInput("ID-" + i, new StringReader(inputs[i]));
        }

        Map<String, SentimentScore> results = analyzer.analyze(analyzerInputs);
        for (Map.Entry<String, SentimentScore> entry : results.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue().getScore());
        }
    }

}