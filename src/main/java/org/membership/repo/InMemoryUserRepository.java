package org.membership.repo;

import org.membership.model.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private final ConcurrentHashMap<String, User> map = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong();

    @Override
    public User save(User user) {
        String id = user.id != null ? user.id : String.valueOf(idGen.incrementAndGet());
        User u = new User(id, user.name, user.cohort);
        map.put(id, u);
        return u;
    }

    @Override public Optional<User> findById(String id) { return Optional.ofNullable(map.get(id)); }
}
