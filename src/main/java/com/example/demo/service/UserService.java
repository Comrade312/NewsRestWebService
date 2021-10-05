package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.user.UsernameReservedException;
import com.example.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Provides CRUD operation with {@link User}
 * Calls a method from {@link UserRepo}
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Find {@link User} object by id
     *
     * @param id {@link User} object to find object by id.
     * @return {@link User} object wrapped into {@link Optional}
     */
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    /**
     * Find all {@link User} objects
     *
     * @return list of {@link User} objects.
     */
    public List<User> findAll() {
        return userRepo.findAll();
    }

    /**
     * Save {@link User} object to save
     *
     * @param user {@link User} object to save
     */
    public void save(User user) {
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new UsernameReservedException(user.getUsername());
        } else {
            user.setActive(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepo.save(user);
        }
    }

    /**
     * Update {@link User} object
     *
     * @param id   {@link User} from request url
     * @param user {@link User} object to update
     * @throws BadRequestParametersException when id from url not equal with id from {@link User} object
     * @throws UserNotFoundException         when there is no {@link User} object in database with provided id
     */
    public void update(Long id, User user) {
        User userDb = userRepo.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getId()));

        if (id.equals(user.getId())) {
            if (!userDb.getUsername().equals(user.getUsername())
                    && userRepo.existsByUsername(user.getUsername())) {
                throw new UsernameReservedException(user.getUsername());
            }
            userRepo.save(user);
        } else {
            throw new BadRequestParametersException("Error in data:" +
                    " path variable id must be not null and equal to user id");
        }
    }

    /**
     * Delete {@link User} object by id
     *
     * @param id {@link User} object to delete
     * @throws UserNotFoundException when there is no {@link User} object in database with provided id
     */
    public void deleteById(Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }
}
