package com.company;

public class Main {

    public static void main(String[] args) {
        String[] words1 = {"The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog."};
        String[] words2 = {"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer."};
        String[] words3 = {"a", "b", "c"};
        String[] words4 = {"", "", ""};

        TextJustifier tj = new TextJustifier();
        String[] justified = tj.justifyText(words4, 1000);
        tj.print(justified);
    }
}
