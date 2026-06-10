package org.membership.repo;

import org.membership.model.MembershipPlan;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPlanRepository implements PlanRepository {
    private final ConcurrentHashMap<String, MembershipPlan> map = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong();

    @Override
    public MembershipPlan save(MembershipPlan plan) {
        String id = plan.id != null ? plan.id : String.valueOf(idGen.incrementAndGet());
        MembershipPlan p = new MembershipPlan(id, plan.name, plan.duration, plan.price, plan.benefits);
        map.put(id, p);
        return p;
    }

    @Override public Optional<MembershipPlan> findById(String id) { return Optional.ofNullable(map.get(id)); }
    @Override public Collection<MembershipPlan> findAll() { return map.values(); }
}
