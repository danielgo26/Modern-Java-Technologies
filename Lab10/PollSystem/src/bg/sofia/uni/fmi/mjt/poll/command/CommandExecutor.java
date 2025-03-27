package bg.sofia.uni.fmi.mjt.poll.command;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private static final int EXPECTED_COUNT_ARGS_ADD_FUNCTION = 3;

    private static final String ADD = "create-poll";
    private static final String SUBMIT = "submit-vote";
    private static final String LIST = "list-polls";
    private static final String DISCONNECT = "disconnect";

    private final PollRepository repository;

    public CommandExecutor(PollRepository repository) {
        this.repository = repository;
    }

    public String execute(Command cmd) {
        return switch (cmd.command()) {
            case ADD -> addPoll(cmd.arguments());
            case SUBMIT -> submit(cmd.arguments());
            case LIST -> list(cmd.arguments());
            case DISCONNECT -> disconnect(cmd.arguments());
            default -> "{\"status\":\"ERROR\",\"message\":\"Unknown command.\"}";
        };
    }

    private String addPoll(String[] args) {
        if (args.length < EXPECTED_COUNT_ARGS_ADD_FUNCTION) {
            return "{\"status\":\"ERROR\",\"message\":\"Invalid count of arguments given.\"}";
        }

        String question = args[0].replaceAll("-", " ");
        Map<String, Integer> options = new HashMap<>();
        for (int i = 1; i < args.length; i++) {
            String option = args[i].replaceAll("-", " ");
            options.put(option, 0);
        }

        int addedPollId = repository.addPoll(new Poll(question, options));
        return "{\"status\":\"OK\",\"message\":\"Poll " + addedPollId + " created successfully.\"}";
    }

    private String submit(String[] args) {
        if (args.length != 2) {
            return "{\"status\":\"ERROR\",\"message\":\"Invalid count of arguments given.\"}";
        }

        Integer pollId;
        try {
            pollId = Integer.valueOf(args[0]);
        } catch (NumberFormatException e) {
            return "{\"status\":\"ERROR\",\"message\":\"The first arg " + args[0] + " is not a valid number.\"}";
        }

        String option = args[1].replaceAll("-", " ");
        Poll poll = repository.getPoll(pollId);
        if (poll == null) {
            return "{\"status\":\"ERROR\",\"message\":\"Poll with ID " + pollId + " does not exist.\"}";
        }

        Integer oldValue = poll.options().get(option);
        if (oldValue == null) {
            return "{\"status\":\"ERROR\",\"message\":\"Invalid option. Option " + option + " does not exist.\"}";
        }

        poll.options().put(option, oldValue + 1);
        return "{\"status\":\"OK\",\"message\":\"Vote submitted successfully for option: " + option + "\"}";
    }

    private String list(String[] args) {
        if (args.length > 0) {
            return "{\"status\":\"ERROR\",\"message\":\"Invalid count of arguments given.\"}";
        }

        var todos = repository.getAllPolls();
        if (todos.isEmpty()) {
            return "{\"status\":\"ERROR\",\"message\":\"No active polls available.\"}";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append("\"status\":\"OK\",\"polls\":[");

        boolean first = true;
        for (var entry : repository.getAllPolls().entrySet()) {
            if (!first) {
                sb.append(",");
            }
            sb.append(getPollStr(entry.getKey(), entry.getValue()));
            first = false;
        }
        sb.append("]");

        return sb.toString();
    }

    private String getPollStr(Integer id, Poll poll) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"");
        sb.append(id);
        sb.append("\": {\"question\":\"");
        sb.append(poll.question());
        sb.append("\",\"options\":{");

        boolean first = true;
        for (var option : poll.options().entrySet()) {
            if (!first) {
                sb.append(",");
            }
            sb.append(addOption(option.getKey(), option.getValue()));
            first = false;
        }

        sb.append("}}}");

        return sb.toString();
    }

    private String addOption(String name, Integer value) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append(name);
        sb.append("\":");
        sb.append(value);

        return sb.toString();
    }

    private String disconnect(String[] args) {
        return "{\"status\":\"OK\",\"message\":\"Disconnected successfully\"}";
    }
}