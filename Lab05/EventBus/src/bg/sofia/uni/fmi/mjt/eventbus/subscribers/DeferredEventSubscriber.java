package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

public class DeferredEventSubscriber<T extends Event<?>> implements Subscriber<T>, Iterable<T> {

    Collection<T> unprocessedEvents;

    public DeferredEventSubscriber() {
        this.unprocessedEvents = new HashSet<>();
    }

    /**
     * Store an event for processing at a later time.
     *
     * @param event the event to be processed
     * @throws IllegalArgumentException if the event is null
     */
    @Override
    public void onEvent(T event) {
        if (event == null) {
            throw new IllegalArgumentException("The given event was null");
        }

        this.unprocessedEvents.add(event);
    }

    /**
     * Get an iterator for the unprocessed events. The iterator should provide the events sorted by
     * their priority in descending order. Events with equal priority are ordered in ascending order
     * of their timestamps.
     *
     * @return an iterator for the unprocessed events
     */
    @Override
    public Iterator<T> iterator() {
        return new UnprocessedEventsIterator();
    }

    private class UnprocessedEventsIterator implements Iterator<T> {

        List<T> events;
        int indexOfNextEvent;

        public UnprocessedEventsIterator() {
            this.events = this.getOrderedEventsCollection();
            this.indexOfNextEvent = 0;
        }

        private List<T> getOrderedEventsCollection() {

            List<T> orderedEvents = new ArrayList<>(unprocessedEvents);

            orderedEvents.sort(new Comparator<T>() {
                @Override
                public int compare(T lhsEvent, T rhsEvent) {
                    int lhsEventPriority = lhsEvent.getPriority();
                    int rhsEventPriority = rhsEvent.getPriority();

                    if (lhsEventPriority == rhsEventPriority) {
                        return rhsEvent.getTimestamp().compareTo(lhsEvent.getTimestamp());
                    } else if (lhsEventPriority > rhsEventPriority) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });

            return orderedEvents;
        }

        @Override
        public boolean hasNext() {
            return this.indexOfNextEvent < this.events.size();
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException("No next element exists");
            }

            return this.events.get(this.indexOfNextEvent++);
        }

    }

    /**
     * Check if there are unprocessed events.
     *
     * @return true if there are unprocessed events, false otherwise
     */
    public boolean isEmpty() {
        return this.unprocessedEvents.isEmpty();
    }

}