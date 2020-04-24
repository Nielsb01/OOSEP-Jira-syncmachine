package nl.avisi.service;

import nl.avisi.dto.LoginDto;
import nl.avisi.exception.InvalidCredentialsException;
import nl.avisi.model.User;

public interface AuthenticationService {
    String login(LoginDto loginCredentials) throws InvalidCredentialsException;
}
