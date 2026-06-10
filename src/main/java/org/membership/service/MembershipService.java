package org.membership.service;

import org.membership.model.*;
import org.membership.repo.*;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MembershipService {
    private final PlanRepository planRepo;
    private final UserRepository userRepo;
    private final SubscriptionRepository subRepo;

    public MembershipService() {
        this.planRepo = new InMemoryPlanRepository();
        this.userRepo = new InMemoryUserRepository();
        this.subRepo = new InMemorySubscriptionRepository();
    }

    @PostConstruct
    public void init() {
        seedSampleData();
    }

    // seed minimal data
    public void seedSampleData() {
        planRepo.save(new MembershipPlan(null, "Monthly Silver", MembershipPlan.Duration.MONTH, new BigDecimal("9.99"),
                List.of("Free delivery over $20", "2% discount")));
        planRepo.save(new MembershipPlan(null, "Quarter Gold", MembershipPlan.Duration.QUARTER, new BigDecimal("24.99"),
                List.of("Free delivery", "5% discount", "Early access")));
        planRepo.save(new MembershipPlan(null, "Year Platinum", MembershipPlan.Duration.YEAR, new BigDecimal("79.99"),
                List.of("Free delivery", "10% discount", "Priority support", "Exclusive deals")));
        userRepo.save(new User(null, "Alice", "cohort-a"));
        userRepo.save(new User(null, "Bob", "cohort-b"));
    }

    public Collection<MembershipPlan> listPlans() { return planRepo.findAll(); }
    public Optional<MembershipPlan> getPlan(String id) { return planRepo.findById(id); }

    public Optional<Subscription> subscribe(String userId, String planId, MemberShipTier tier) {
        var u = userRepo.findById(userId);
        var p = planRepo.findById(planId);
        if (u.isEmpty() || p.isEmpty()) return Optional.empty();

        Instant start = Instant.now();
        Instant expires = switch (p.get().duration) {
            case MONTH -> start.plus(30, ChronoUnit.DAYS);
            case QUARTER -> start.plus(90, ChronoUnit.DAYS);
            case YEAR -> start.plus(365, ChronoUnit.DAYS);
        };
        var s = new Subscription(null, userId, planId, tier, start, expires);
        return Optional.of(subRepo.save(s));
    }

    public Optional<Subscription> getSubscription(String id) { return subRepo.findById(id); }

    public List<Subscription> getByUser(String userId) { return subRepo.findByUserId(userId); }

    public Optional<Subscription> cancel(String id) {
        var s = subRepo.findById(id);
        if (s.isEmpty()) return Optional.empty();
        var ss = s.get();
        ss.status = Subscription.Status.CANCELLED;
        return Optional.of(subRepo.save(ss));
    }

    public Optional<Subscription> upgrade(String id) {
        var s = subRepo.findById(id);
        if (s.isEmpty()) return Optional.empty();
        var ss = s.get();
        if (ss.tier == MemberShipTier.PLATINUM) return Optional.of(ss);
        ss.tier = switch (ss.tier) {
            case SILVER -> MemberShipTier.GOLD;
            case GOLD -> MemberShipTier.PLATINUM;
            default -> ss.tier;
        };
        return Optional.of(subRepo.save(ss));
    }

    public Optional<Subscription> downgrade(String id) {
        var s = subRepo.findById(id);
        if (s.isEmpty()) return Optional.empty();
        var ss = s.get();
        if (ss.tier == MemberShipTier.SILVER) return Optional.of(ss);
        ss.tier = switch (ss.tier) {
            case PLATINUM -> MemberShipTier.GOLD;
            case GOLD -> MemberShipTier.SILVER;
            default -> ss.tier;
        };
        return Optional.of(subRepo.save(ss));
    }
}
