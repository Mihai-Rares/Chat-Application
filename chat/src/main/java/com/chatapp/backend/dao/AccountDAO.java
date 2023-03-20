package com.chatapp.backend.dao;

import com.chatapp.backend.model.User;
import com.chatapp.backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A Spring Data JPA repository for managing {@link Account} entities.
 */
@Repository
public interface AccountDAO extends JpaRepository<Account, Long> {
    /**
     * Finds an {@link Account} entity by the given {@link User}.
     *
     * @param user the user associated with the account
     * @return an optional {@link Account} entity that matches the user, or an empty optional if not found
     */
    Optional<Account> findByUser(User user);
}
