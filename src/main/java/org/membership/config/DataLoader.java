package org.membership.config;

import org.membership.model.MembershipPlan;
import org.membership.model.User;
import org.membership.repo.PlanRepository;
import org.membership.repo.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

@Configuration
@Profile("default")
public class DataLoader {
    private final PlanRepository planRepo;
    private final UserRepository userRepo;

    public DataLoader(PlanRepository planRepo, UserRepository userRepo) {
        this.planRepo = planRepo; this.userRepo = userRepo;
    }

    @PostConstruct
    public void load() {
        planRepo.save(new MembershipPlan(null, "Monthly - Silver", MembershipPlan.Duration.MONTH, new BigDecimal("9.99"), List.of("Free delivery over $20")));
        planRepo.save(new MembershipPlan(null, "Monthly - Gold", MembershipPlan.Duration.MONTH, new BigDecimal("19.99"), List.of("Free delivery", "5% discount")));
        planRepo.save(new MembershipPlan(null, "Monthly - Platinum", MembershipPlan.Duration.MONTH, new BigDecimal("29.99"), List.of("Free delivery", "10% discount", "priority support")));

        planRepo.save(new MembershipPlan(null, "Quarter - Silver", MembershipPlan.Duration.QUARTER, new BigDecimal("24.99"), List.of("Free delivery over $20")));
        planRepo.save(new MembershipPlan(null, "Quarter - Gold", MembershipPlan.Duration.QUARTER, new BigDecimal("49.99"), List.of("Free delivery", "5% discount")));
        planRepo.save(new MembershipPlan(null, "Quarter - Platinum", MembershipPlan.Duration.QUARTER, new BigDecimal("79.99"), List.of("Free delivery", "10% discount", "priority support")));

        planRepo.save(new MembershipPlan(null, "Year - Silver", MembershipPlan.Duration.YEAR, new BigDecimal("79.99"), List.of("Free delivery over $20")));
        planRepo.save(new MembershipPlan(null, "Year - Gold", MembershipPlan.Duration.YEAR, new BigDecimal("149.99"), List.of("Free delivery", "5% discount")));
        planRepo.save(new MembershipPlan(null, "Year - Platinum", MembershipPlan.Duration.YEAR, new BigDecimal("249.99"), List.of("Free delivery", "10% discount", "priority support")));

        userRepo.save(new User(null, "Alice", "cohort-a"));
        userRepo.save(new User(null, "Bob", "cohort-b"));
    }
}
