package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.basicStopWordsText;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextTokenizerTest {

    private static final TextTokenizer tokenizer =
        new TextTokenizer(new StringReader(basicStopWordsText));

    @Test
    void testTextTokenizerSeparatesCorrectlyStopWordsFromStream() {
        Set<String> resulted = new TextTokenizer(
            new StringReader("a" + System.lineSeparator() + "the" + System.lineSeparator() + "of")).stopwords();

        Set<String> expected = Set.of("a", "the", "of");

        assertEquals(resulted.size(), expected.size(),
            "Expected to return set with size 2, but returned with size: " + resulted);
        assertTrue(resulted.containsAll(expected),
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

    @Test
    void testTokenizeShouldThrowWhenTheGivenInputIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> tokenizer.tokenize(null),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testTokenizeShouldReturnValidCollectionOfTokenizedWords() {
        List<String> resulted = tokenizer.tokenize("Hello,    there my-friend,   the best!");

        List<String> expected = List.of("hello", "there", "myfriend", "best");

        assertEquals(resulted, expected,
            "Expected to return: " + expected + ", but returned: " + resulted);

    }

    @Test
    void testTokenizeWithoutRemovingStopwordsShouldThrowWhenTheGivenCollectionIsNull(){
        assertThrows(IllegalArgumentException.class,
            () -> tokenizer.tokenizeWithoutRemovingStopwords(null),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testTokenizeWithoutRemovingStopwordsShouldThrowWhenTheGivenCollectionContainsNull(){
        Set<String> toTokenize = new HashSet<>();
        toTokenize.add(null);

        assertThrows(IllegalArgumentException.class,
            () -> tokenizer.tokenizeWithoutRemovingStopwords(toTokenize),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testTokenizeWithoutRemovingStopwordsShouldReturnValidCollectionOfTokenizedWords(){
        List<String> toTokenize = List.of("Hello   , ThEre!","You aRe the greatest","the-giver");
        List<String> resulted = tokenizer.tokenizeWithoutRemovingStopwords(toTokenize);

        List<String> expected = List.of("hellothere", "youarethegreatest", "thegiver");

        assertEquals(resulted, expected,
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

}