package org.membership.service;

import org.membership.model.Purchase;
import org.membership.model.User;
import org.membership.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> find(String id) { return userRepo.findById(id); }
    public User save(User u) { return userRepo.save(u); }
    public void recordPurchase(String userId, double amount) {
        Optional<User> u = userRepo.findById(userId);
        if (u.isEmpty()) return;
        u.get().recordPurchase(new Purchase(amount, java.time.Instant.now()));
    }
}
