package com.example.demo.facade;

import static com.example.demo.dto.RegistrationRequestProto.RegistrationRequestDto;

/**
 * Interface for basic authorization
 */
public interface AuthBasicFacade {
    /**
     * Method for user registration. Create user with role SUBSCRIBER
     *
     * @param request {@link RegistrationRequestDto} object, contains username and password
     */
    void registration(RegistrationRequestDto request);
}
