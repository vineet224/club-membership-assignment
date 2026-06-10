package org.membership.service;

import org.membership.model.MemberShipTier;
import org.membership.model.Subscription;
import org.membership.model.MembershipPlan;
import org.membership.model.User;
import org.membership.model.Purchase;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class MembershipService {
    private final PlanService planService;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public MembershipService(PlanService planService, UserService userService, SubscriptionService subscriptionService) {
        this.planService = planService;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    public Collection<MembershipPlan> listPlans() { return planService.listPlans(); }
    public Optional<MembershipPlan> getPlan(String id) { return Optional.ofNullable(planService.get(id)); }

    public Optional<Subscription> subscribe(String userId, String planId, MemberShipTier tier) {
        MembershipPlan plan = planService.get(planId);
        if (plan == null) return Optional.empty();
        Subscription s = new Subscription(null, userId, planId, tier, java.time.Instant.now(), java.time.Instant.now());
        return Optional.of(subscriptionService.save(s));
    }

    public Optional<Subscription> getSubscription(String id) { return subscriptionService.get(id); }
    public List<Subscription> getByUser(String userId) { return subscriptionService.getByUser(userId); }
    public Optional<Subscription> cancel(String id) { return subscriptionService.cancel(id); }
    public Optional<Subscription> upgrade(String id) { return subscriptionService.upgrade(id); }
    public Optional<Subscription> downgrade(String id) { return subscriptionService.downgrade(id); }

    
    public boolean recordPurchaseAndEvaluate(String userId, double amount) {
        userService.recordPurchase(userId, amount);
        Optional<User> uOpt = userService.find(userId);
        if (uOpt.isEmpty()) return false;
        User u = uOpt.get();
        List<Purchase> in30 = u.purchases.stream()
                .filter(p -> p.at.isAfter(java.time.Instant.now().minus(30, java.time.temporal.ChronoUnit.DAYS)))
                .toList();
        double total = in30.stream().mapToDouble(p -> p.amount).sum();
        int count = in30.size();
        MemberShipTier newTier = MemberShipTier.SILVER;
        if (count >= 10 || total >= 1000) newTier = MemberShipTier.PLATINUM;
        else if (count >= 5 || total >= 300) newTier = MemberShipTier.GOLD;
        List<Subscription> subs = subscriptionService.getByUser(userId);
        for (Subscription s : subs) {
            if (s.status != Subscription.Status.ACTIVE) continue;
            s.tier = newTier;
            subscriptionService.save(s);
        }
        return true;
    }
}
