package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.user.UsernameReservedException;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.UserCrudService;
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
public class UserService implements UserDetailsService, UserCrudService {
    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public void save(User user) {
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new UsernameReservedException(user.getUsername());
        } else {
            user.setActive(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepo.save(user);
        }
    }

    @Override
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

    @Override
    public void deleteById(Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }
}
