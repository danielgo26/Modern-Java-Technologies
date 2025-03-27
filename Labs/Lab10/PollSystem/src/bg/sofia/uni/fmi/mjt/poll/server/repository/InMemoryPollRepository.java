package bg.sofia.uni.fmi.mjt.poll.server.repository;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;

import java.util.HashMap;
import java.util.Map;

public class InMemoryPollRepository implements PollRepository {

    private HashMap<Integer, Poll> polls;
    private Integer lastPollId;

    public InMemoryPollRepository() {
        polls = new HashMap<>();
        lastPollId = 1;
    }

    @Override
    public int addPoll(Poll poll) {
        polls.put(lastPollId, poll);
        return lastPollId++;
    }

    @Override
    public Poll getPoll(int pollId) {
        return polls.get(pollId);
    }

    @Override
    public Map<Integer, Poll> getAllPolls() {
        return polls;
    }

    @Override
    public void clearAllPolls() {
        polls = new HashMap<>();
        lastPollId = 1;
    }

}