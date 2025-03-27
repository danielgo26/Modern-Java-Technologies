package bg.sofia.uni.fmi.mjt.poll.client;

import bg.sofia.uni.fmi.mjt.poll.server.PollServer;
import bg.sofia.uni.fmi.mjt.poll.server.repository.InMemoryPollRepository;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

public class Run {
    private static final int SERVER_PORT = 7777;

    public static void main(String[] args) {
        PollRepository repo = new InMemoryPollRepository();
        PollServer server = new PollServer(SERVER_PORT, repo);
        server.start();
    }
}