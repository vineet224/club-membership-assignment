package org.membership.config;

import org.membership.repo.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepoConfig {
    @Bean
    public PlanRepository planRepository() {
        return new InMemoryPlanRepository();
    }

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    public SubscriptionRepository subscriptionRepository() {
        return new InMemorySubscriptionRepository();
    }
}
