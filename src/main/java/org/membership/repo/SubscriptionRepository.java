package org.membership.repo;

import org.membership.model.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    Subscription save(Subscription s);
    Optional<Subscription> findById(String id);
    List<Subscription> findByUserId(String userId);
    void delete(String id);
}
