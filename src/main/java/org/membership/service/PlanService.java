package org.membership.service;

import org.membership.model.MembershipPlan;
import org.membership.repo.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PlanService {
    private final PlanRepository planRepo;

    public PlanService(PlanRepository planRepo) {
        this.planRepo = planRepo;
    }

    public Collection<MembershipPlan> listPlans() { return planRepo.findAll(); }
    public MembershipPlan get(String id) { return planRepo.findById(id).orElse(null); }
}
