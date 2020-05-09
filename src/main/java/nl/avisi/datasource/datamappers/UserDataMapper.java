package nl.avisi.datasource.datamappers;

import nl.avisi.dto.LoginDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDataMapper implements DataMapper<LoginDTO> {
    @Override
    public LoginDTO toDTO(ResultSet resultSet) throws SQLException {
        LoginDTO loginDTO = new LoginDTO();

        while (resultSet.next()) {
            loginDTO = new LoginDTO().
                    setUsername(resultSet.getString("Username"))
                    .setPassword(resultSet.getString("Password"));

        }
        return loginDTO;
    }
}
