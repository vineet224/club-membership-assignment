package org.membership.repo;

import org.membership.model.MembershipPlan;

import java.util.Collection;
import java.util.Optional;

public interface PlanRepository {
    MembershipPlan save(MembershipPlan plan);
    Optional<MembershipPlan> findById(String id);
    Collection<MembershipPlan> findAll();
}
