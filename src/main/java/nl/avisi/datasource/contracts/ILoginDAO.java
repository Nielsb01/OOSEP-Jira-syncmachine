package nl.avisi.datasource.contracts;

import nl.avisi.dto.LoginDTO;

public interface ILoginDAO {
    LoginDTO getLoginInfo(String userLogin);
}
