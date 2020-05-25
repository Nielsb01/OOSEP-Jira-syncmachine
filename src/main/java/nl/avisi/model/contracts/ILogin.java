package nl.avisi.model.contracts;

import nl.avisi.dto.LoginDTO;

public interface ILogin {
    int validateCredentials(LoginDTO loginDTO);
}
