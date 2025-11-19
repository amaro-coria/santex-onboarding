package com.example.demo.mapper;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper utility for converting between User entity and DTOs.
 */
@Component
public class UserMapper {

    /**
     * Convert User entity to UserResponse DTO.
     *
     * @param user the user entity
     * @return the user response DTO
     */
    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    /**
     * Convert UserRequest DTO to User entity.
     *
     * @param request the user request DTO
     * @return the user entity
     */
    public User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }

        return new User(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName()
        );
    }

    /**
     * Update existing User entity with data from UserRequest DTO.
     *
     * @param user the existing user entity
     * @param request the update request DTO
     */
    public void updateEntityFromRequest(User user, UserRequest request) {
        if (user == null || request == null) {
            return;
        }

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        // Only update password if provided and not empty
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(request.getPassword());
        }
    }
}
