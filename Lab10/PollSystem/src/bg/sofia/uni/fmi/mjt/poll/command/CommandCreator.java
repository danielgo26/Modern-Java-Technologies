package bg.sofia.uni.fmi.mjt.poll.command;

import java.util.Arrays;
import java.util.List;

public class CommandCreator {

    public static Command newCommand(String clientInput) {
        List<String> tokens = CommandCreator.getCommandArr(clientInput);
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("The given command is not in the valid syntax");
        }

        return new Command(tokens.getFirst(), CommandCreator.getCommandArguments(tokens));
    }

    private static List<String> getCommandArr(String input) {
        return Arrays.stream(input.split(" "))
            .map(t -> t.replaceAll(System.lineSeparator(), ""))
            .parallel()
            .toList();
    }

    private static String[] getCommandArguments(List<String> arr) {
        return arr
            .stream()
            .parallel()
            .skip(1)
            .map(t -> t.replaceAll("-", " "))
            .map(t -> t.replaceAll(System.lineSeparator(), ""))
            .toList()
            .toArray(new String[0]);
    }

}