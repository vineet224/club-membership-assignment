package org.membership.repo;


import org.membership.model.Subscription;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemorySubscriptionRepository implements SubscriptionRepository {
    private final ConcurrentHashMap<String, Subscription> map = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong();

    @Override
    public Subscription save(Subscription s) {
        String id = s.id != null ? s.id : String.valueOf(idGen.incrementAndGet());
        var out = new Subscription(id, s.userId, s.planId, s.tier, s.startAt, s.expiresAt);
        out.status = s.status;
        map.put(id, out);
        return out;
    }

    @Override public Optional<Subscription> findById(String id) { return Optional.ofNullable(map.get(id)); }

    @Override
    public List<Subscription> findByUserId(String userId) {
        return map.values().stream().filter(x -> x.userId.equals(userId)).collect(Collectors.toList());
    }

    @Override public void delete(String id) { map.remove(id); }
}
