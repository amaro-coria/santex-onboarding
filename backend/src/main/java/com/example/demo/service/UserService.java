package com.example.demo.service;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;

import java.util.List;

/**
 * Service interface for User entity operations.
 * Defines business logic methods for user management.
 */
public interface UserService {

    /**
     * Create a new user.
     *
     * @param request the user creation request
     * @return the created user response
     * @throws com.example.demo.exception.DuplicateResourceException if email already exists
     */
    UserResponse createUser(UserRequest request);

    /**
     * Get a user by their ID.
     *
     * @param id the user ID
     * @return the user response
     * @throws com.example.demo.exception.ResourceNotFoundException if user not found
     */
    UserResponse getUserById(Long id);

    /**
     * Get a user by their email address.
     *
     * @param email the user's email
     * @return the user response
     * @throws com.example.demo.exception.ResourceNotFoundException if user not found
     */
    UserResponse getUserByEmail(String email);

    /**
     * Get all users in the system.
     *
     * @return list of all users
     */
    List<UserResponse> getAllUsers();

    /**
     * Update an existing user.
     *
     * @param id the user ID
     * @param request the update request
     * @return the updated user response
     * @throws com.example.demo.exception.ResourceNotFoundException if user not found
     * @throws com.example.demo.exception.DuplicateResourceException if email already exists
     */
    UserResponse updateUser(Long id, UserRequest request);

    /**
     * Delete a user by ID.
     *
     * @param id the user ID
     * @throws com.example.demo.exception.ResourceNotFoundException if user not found
     */
    void deleteUser(Long id);

    /**
     * Check if a user exists by email.
     *
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
}
