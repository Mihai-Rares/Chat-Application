package com.chatapp.backend.service;

import com.chatapp.backend.dao.AccountDAO;
import com.chatapp.backend.exception.NotFoundException;
import com.chatapp.backend.model.Account;
import com.chatapp.backend.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The AccountService class handles business logic related to the Account model, interacting with the
 * <p>
 * persistence layer through the injected AccountDAO instance. It provides methods for saving, retrieving,
 * <p>
 * and deleting Account objects, as well as updating an account's image.
 */
@Service
@Slf4j
public class AccountService {
    @Autowired
    private AccountDAO accountDAO;

    /**
     * Saves the given Account object to the persistence layer.
     *
     * @param account the Account object to save
     * @return the saved Account object
     */
    public Account saveAccount(Account account) {
        log.info("Saving account: {}", account);
        return accountDAO.save(account);
    }

    /**
     * Retrieves the Account object associated with the given User object from the persistence layer.
     *
     * @param user the User object associated with the Account object to retrieve
     * @return an Optional containing the retrieved Account object, or an empty Optional if no Account object is found
     */
    public Optional<Account> getAccountByUser(User user) {
        log.info("Getting account for user: {}", user);
        return accountDAO.findByUser(user);
    }

    /**
     * Deletes the given Account object from the persistence layer.
     *
     * @param account the Account object to delete
     */
    public void deleteAccount(Account account) {
        log.info("Deleting account: {}", account);
        accountDAO.delete(account);
    }

    /**
     * Updates the image, name, and extension of the Account object with the given ID in the persistence layer.
     *
     * @param accountId the ID of the Account object to update
     * @param image     the updated image data for the Account object
     * @param name      the updated name of the image file for the Account object
     * @param extension the updated extension of the image file for the Account object
     * @return the updated Account object
     * @throws NotFoundException if no Account object is found with the given ID
     */
    public Account updateImage(Long accountId, byte[] image, String name, String extension) {
        Account account = accountDAO.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + accountId));

        account.setImage(image);
        account.setName(name);
        account.setExtension(extension);
        Account updatedAccount = accountDAO.save(account);

        log.info("Account image updated with id: {}", accountId);
        return updatedAccount;
    }

    /**
     * Updates the description of the specified Account entity in the database.
     *
     * @param accountId   the ID of the Account entity to update
     * @param description the new description to set for the Account entity
     * @return the updated Account entity
     * @throws NotFoundException if the Account entity with the specified ID is not found
     */
    public Account updateDescription(Long accountId, String description) {
        Account account = accountDAO.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + accountId));

        account.setDescription(description);
        Account updatedAccount = accountDAO.save(account);

        log.info("Account description updated with id: {}", accountId);
        return updatedAccount;
    }

    /**
     * Deletes the image of the specified Account entity in the database.
     *
     * @param accountId the ID of the Account entity to update
     * @return the updated Account entity
     * @throws NotFoundException if the Account entity with the specified ID is not found
     */
    public Account deleteImage(Long accountId) {
        Account account = accountDAO.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + accountId));

        account.setImage(null);
        account.setName(null);
        account.setExtension(null);
        Account updatedAccount = accountDAO.save(account);

        log.info("Account image deleted with id: {}", accountId);
        return updatedAccount;
    }
}
