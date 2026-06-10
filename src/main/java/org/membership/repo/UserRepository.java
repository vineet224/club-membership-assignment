package org.membership.repo;

import org.membership.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String id);
}
