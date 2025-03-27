package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBusImpl implements EventBus {

    private final Map<Class<?>, Set<Subscriber<?>>> subscriptions;
    private final Collection<Event<?>> occuredEvents;

    public EventBusImpl() {
        this.subscriptions = new HashMap<>();
        this.occuredEvents = new HashSet<>();
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null) {
            throw new IllegalArgumentException("The event type given for a creation of a subscription was null");
        }
        if (subscriber == null) {
            throw new IllegalArgumentException("The subscriber given for a creation of a subscription was null");
        }

        if (this.subscriptions.containsKey(eventType)) {
            this.subscriptions.get(eventType).add(subscriber);
        } else {
            this.subscriptions.put(eventType, new HashSet<>());
            this.subscriptions.get(eventType).add(subscriber);
        }
    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
        throws MissingSubscriptionException {
        if (eventType == null) {
            throw new IllegalArgumentException("The event type given for a removal of a subscription was null");
        }
        if (subscriber == null) {
            throw new IllegalArgumentException("The subscriber given for a removal of a subscription was null");
        }

        if (!(this.subscriptions.containsKey(eventType) && this.subscriptions.get(eventType).contains(subscriber))) {
            throw new MissingSubscriptionException("The given subscriber was not subscribed for the given event type");
        }

        this.subscriptions.get(eventType).remove(subscriber);
    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("The given event to be published was null");
        }

        if (this.subscriptions.containsKey(event.getClass())) {
            for (Subscriber<?> subscriber : this.subscriptions.get(event.getClass())) {
                if (subscriber != null) {
                    @SuppressWarnings("unchecked")
                    Subscriber<T> castSubscriber = (Subscriber<T>) subscriber;
                    if (castSubscriber != null) {
                        castSubscriber.onEvent(event);
                    }
                }
            }
        }
        this.occuredEvents.add(event);
    }

    @Override
    public void clear() {
        this.subscriptions.clear();
        this.occuredEvents.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        if (eventType == null)  {
            throw new IllegalArgumentException("While getting the event logs the event type was null");
        }
        if (from == null)  {
            throw new IllegalArgumentException("While getting the event logs the event type was null");
        }
        if (to == null)  {
            throw new IllegalArgumentException("While getting the event logs the event type was null");
        }
        if (from.compareTo(to) >= 0) {
            return Collections.emptySet();
        }
        List<Event<?>> eventLogs = new ArrayList<>();
        for (Event<?> event : this.occuredEvents) {
            if (event != null &&
                event.getClass().equals(eventType) &&
                event.getTimestamp().compareTo(from) >= 0 &&
                event.getTimestamp().compareTo(to) < 0) {
                eventLogs.add(event);
            }
        }
        eventLogs.sort(new Comparator<Event<?>>() {
            @Override
            public int compare(Event<?> lhs, Event<?> rhs) {
                return lhs.getTimestamp().compareTo(rhs.getTimestamp());
            }
        });
        return Collections.unmodifiableCollection(eventLogs);
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type was null while getting subscribers for an event");
        }

        if (!this.subscriptions.containsKey(eventType)) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableCollection(this.subscriptions.get(eventType));
    }
}