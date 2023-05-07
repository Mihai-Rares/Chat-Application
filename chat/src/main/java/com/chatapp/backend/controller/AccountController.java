package com.chatapp.backend.controller;

import com.chatapp.backend.model.Account;
import com.chatapp.backend.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import com.chatapp.backend.model.User;
import com.chatapp.backend.service.UserService;
import com.chatapp.backend.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.PrinterName;
import java.io.IOException;
import java.security.Principal;
import java.util.Locale;
import java.util.Optional;

/**
 * Controller class to handle HTTP requests related to accounts.
 * <p>
 * This class handles CRUD operations for user accounts, including account image updates and deletions.
 * <p>
 * It also handles authentication and authorization via the Principal object.
 *
 * @author Mihai Rares
 * @version 1.0
 * @since 20/3/2023
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    /**
     * Handles GET requests to retrieve an Account object by the associated user.
     *
     * @param user Principal object representing the authenticated user.
     * @return ResponseEntity object containing the requested Account object or an error message.
     */
    @GetMapping("/")
    public ResponseEntity<Account> getAccountByUser(Principal user) {
        Optional<Account> account = accountService
                .getAccountByUser(userService.getUserWithUsername(user.getName()));
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Handles POST requests to create a new Account object.
     *
     * @param user    Principal object representing the authenticated user.
     * @param account Account object to be created.
     * @return ResponseEntity object containing the newly created Account object or an error message.
     */
    @PostMapping("/")
    public ResponseEntity<Account> createAccount(Principal user, @RequestBody Account account) {
        account.setUser(userService.getUserWithUsername(user.getName()));
        Account savedAccount = accountService.saveAccount(account);
        return ResponseEntity.ok(savedAccount);
    }

    /**
     * Handles PUT requests to update an existing Account object.
     *
     * @param user    Principal object representing the authenticated user.
     * @param account Account object containing updated information.
     * @return ResponseEntity object containing the updated Account object or an error message.
     */
    @PutMapping("/")
    public ResponseEntity<Account> updateAccount(Principal user, @RequestBody Account account) {
        account.setUser(userService.getUserWithUsername(user.getName()));
        Account updatedAccount = accountService.saveAccount(account);
        return ResponseEntity.ok(updatedAccount);
    }

    /**
     * Handles DELETE requests to delete an existing Account object.
     *
     * @param user Principal object representing the authenticated user.
     * @return ResponseEntity object indicating whether the operation was successful or an error occurred.
     */
    @DeleteMapping("/")
    public ResponseEntity<Void> deleteAccount(Principal user) {
        Optional<Account> account = accountService
                .getAccountByUser(userService.getUserWithUsername(user.getName()));
        account.ifPresent(accountService::deleteAccount);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handles POST requests to update an Account object's image.
     *
     * @param user Principal object representing the authenticated user.
     * @param file MultipartFile object containing the new image file.
     * @return ResponseEntity object containing the updated Account object or an error message.
     * @throws IOException if there was an error reading the MultipartFile object.
     */
    @PostMapping("/image")
    public ResponseEntity<Account> updateImage(Principal user,
                                               @RequestParam("image") MultipartFile file) throws IOException {
        Optional<Account> optionalAccount = accountService
                .getAccountByUser(userService.getUserWithUsername(user.getName()));
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            byte[] imageBytes = file.getBytes();
            String filename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(filename);

            account.setImage(imageBytes);
            account.setName(filename);
            account.setExtension(extension);

            Account updatedAccount = accountService.saveAccount(account);

            return ResponseEntity.ok(updatedAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes the image associated with the currently authenticated user's account.
     *
     * @param user the authenticated user
     * @return a {@link ResponseEntity} with status 200 and the updated {@link Account} object if the image was deleted
     * successfully, or a {@link ResponseEntity} with status 404 if the user does not have an account or the account
     * does not have an image
     */
    @DeleteMapping("/image")
    public ResponseEntity<Account> deleteImage(Principal user) {
        Optional<Account> optionalAccount = accountService
                .getAccountByUser(userService.getUserWithUsername(user.getName()));
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setImage(null);
            account.setName(null);
            account.setExtension(null);
            Account updatedAccount = accountService.saveAccount(account);
            return ResponseEntity.ok(updatedAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}