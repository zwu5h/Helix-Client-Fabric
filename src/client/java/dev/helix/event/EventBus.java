package dev.helix.event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class EventBus {
    private final Map<Class<?>, List<Subscription<?>>> subscriptions = new ConcurrentHashMap<>();

    public <T extends Event> void subscribe(Class<T> type, Consumer<T> listener) {
        subscribe(type, 0, listener);
    }

    public <T extends Event> void subscribe(Class<T> type, int priority, Consumer<T> listener) {
        subscriptions.computeIfAbsent(type, ignored -> new ArrayList<>())
                .add(new Subscription<>(priority, listener));
        subscriptions.get(type).sort(Comparator.comparingInt((Subscription<?> subscription) -> subscription.priority()).reversed());
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void post(T event) {
        List<Subscription<?>> listeners = subscriptions.get(event.getClass());
        if (listeners == null) {
            return;
        }

        for (Subscription<?> subscription : List.copyOf(listeners)) {
            ((Consumer<T>) subscription.listener()).accept(event);
        }
    }

    private record Subscription<T extends Event>(int priority, Consumer<T> listener) {
    }
}
