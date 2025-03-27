package com.company;

public class TextJustifier {
    public static String[] justifyText(String[] words, int maxWidth) {
        //get the size of the justified array
        int justifiedWordsArraySize = 0;
        int currentCharactersPerLine = 0;
        String word = "";
        boolean onlyEmptyWords = true;
        for (int i = 0; i < words.length; i++) {
            word = words[i];
            if (!word.equals(""))
                onlyEmptyWords = false;
            if (currentCharactersPerLine == 0) {
                currentCharactersPerLine += word.length();
                continue;
            }
            if (currentCharactersPerLine + 1 + word.length() > maxWidth) {
                justifiedWordsArraySize++;
                i--;
                currentCharactersPerLine = 0;
            } else {
                currentCharactersPerLine += 1 + word.length();
            }
        }
        justifiedWordsArraySize++;

        if (onlyEmptyWords)
            return new String[0];

        String[] justifiedWordsArray = new String[justifiedWordsArraySize];

        //fill the justified array
        int from = 0, to = 0;
        currentCharactersPerLine = 0;
        int justifiedArrayIndex = 0;
        word = "";
        for (int i = 0; i < words.length; i++) {
            word = words[i];

            int potentialCharactersCount = currentCharactersPerLine + word.length();
            if (currentCharactersPerLine != 0)
                potentialCharactersCount++;
            if (potentialCharactersCount > maxWidth || i == words.length - 1) {
                if (i != words.length - 1 || potentialCharactersCount > maxWidth)
                    i--;
                to = i;

                if (i == words.length - 1)
                    currentCharactersPerLine = potentialCharactersCount;

                //fill in the justified array

                int countWordsIncludedInLine = to - from + 1;
                int countExtraSpace = maxWidth - currentCharactersPerLine;
                //create buffer to save the current line
                StringBuilder currentLine = new StringBuilder(maxWidth);
                if (countWordsIncludedInLine == 1) {
                    //copy word into buffer
                    currentLine.append(words[i]);
                    //set intervals at the end
                    for (int j = 0; j < countExtraSpace; j++) {
                        currentLine.append(' ');
                    }
                } else {
                    //we have gathered all the rest words in the last line
                    if (i == words.length - 1) {
                        //append each word to the buffer
                        int indexInBuffer = 0;
                        for (int j = from; j <= to; j++) {
                            currentLine.append(words[j]);
                            if (j < to) {
                                currentLine.append(' ');
                            }
                        }
                        currentCharactersPerLine = potentialCharactersCount;
                        //append intervals until end of line
                        for (int j = 0; j < maxWidth - currentCharactersPerLine; j++) {
                            currentLine.append(' ');
                        }
                    } else {
                        int countIntervalsPerEachFreeSpace = countExtraSpace / (countWordsIncludedInLine - 1);
                        int countRestIntervals = countExtraSpace % (countWordsIncludedInLine - 1);
                        //append each word to the buffer
                        int indexInBuffer = 0;
                        for (int j = from; j <= to; j++) {
                            currentLine.append(words[j]);
                            if (j != to) {
                                //append the 'default' interval between words
                                currentLine.append(' ');
                                //append the count of intervals depending on the free space position
                                int countIntervalsInCurrentFreeSpace = countIntervalsPerEachFreeSpace + (countRestIntervals > 0 ? 1 : 0);
                                if (countRestIntervals > 0)
                                    countRestIntervals--;
                                for (int k = 0; k < countIntervalsInCurrentFreeSpace; k++) {
                                    currentLine.append(' ');
                                }
                            }
                        }
                    }
                }
                //save the justified line into the justified array
                justifiedWordsArray[justifiedArrayIndex++] = currentLine.toString();

                currentCharactersPerLine = 0;
                from = i + 1;
            } else {
                currentCharactersPerLine = potentialCharactersCount;
            }
        }

        //return the justified array
        return justifiedWordsArray;
    }

    public static void print(String[] words) {
        for (String word : words) {
            System.out.println(word);
        }
    }
}
