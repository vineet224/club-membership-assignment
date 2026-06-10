package org.membership.service;

import org.membership.model.MemberShipTier;
import org.membership.model.Subscription;
import org.membership.repo.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subRepo;

    public SubscriptionService(SubscriptionRepository subRepo) {
        this.subRepo = subRepo;
    }

    public Optional<Subscription> get(String id) { return subRepo.findById(id); }
    public List<Subscription> getByUser(String userId) { return subRepo.findByUserId(userId); }
    public Subscription save(Subscription s) { return subRepo.save(s); }

    // Haven't implemented the logic for concurrency control like if the subscription is updated and subscription is being cancelled at the same time. or subscription is updated at same time by two threads.
    public Optional<Subscription> cancel(String id) {
        Optional<Subscription> s = subRepo.findById(id);
        if (s.isEmpty()) return Optional.empty();
        Subscription ss = s.get(); ss.status = Subscription.Status.CANCELLED; return Optional.of(subRepo.save(ss));


    }
    // Haven't implemented the logic for concurrency control like if the subscription is updated and subscription is being cancelled at the same time. or subscription is updated at same time by two threads.
    public Optional<Subscription> upgrade(String id) {
        Optional<Subscription> s = subRepo.findById(id);
        if (s.isEmpty()) return Optional.empty();
        Subscription ss = s.get();
        if (ss.tier == MemberShipTier.PLATINUM) return Optional.of(ss);
        ss.tier = switch (ss.tier) {
            case SILVER -> MemberShipTier.GOLD;
            case GOLD -> MemberShipTier.PLATINUM;
            default -> ss.tier;
        };
        return Optional.of(subRepo.save(ss));
    }
    // Haven't implemented the logic for concurrency control like if the subscription is updated and subscription is being cancelled at the same time. or subscription is updated at same time by two threads.
    public Optional<Subscription> downgrade(String id) {
        Optional<Subscription> s = subRepo.findById(id);
        if (s.isEmpty()) return Optional.empty();
        Subscription ss = s.get();
        if (ss.tier == MemberShipTier.SILVER) return Optional.of(ss);
        ss.tier = switch (ss.tier) {
            case PLATINUM -> MemberShipTier.GOLD;
            case GOLD -> MemberShipTier.SILVER;
            default -> ss.tier;
        };
        return Optional.of(subRepo.save(ss));
    }
}
