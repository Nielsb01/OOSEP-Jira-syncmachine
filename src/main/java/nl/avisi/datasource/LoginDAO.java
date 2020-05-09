package nl.avisi.datasource;

import nl.avisi.datasource.contracts.ILoginDAO;
import nl.avisi.datasource.datamapper.DataMapper;
import nl.avisi.dto.LoginDTO;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO implements ILoginDAO {
    private DataMapper<LoginDTO> loginDataMapper;
    private Database database;

    private final static String getLoginDataSql = "SELECT * FROM SyncMachineAccounts WHERE UserID = ?";

    @Inject
    public void setDatabase(Database database) {
        this.database = database;
    }

    @Inject
    public void setLoginDataMapper(DataMapper<LoginDTO> loginDataMapper) {
        this.loginDataMapper = loginDataMapper;
    }

    @Override
    public LoginDTO getLoginInfo(String userLogin) {

        try (Connection connection = database.connect(); PreparedStatement stmt = connection.prepareStatement(getLoginDataSql)) {
            stmt.setString(1, userLogin);

            return loginDataMapper.toDTO(stmt.executeQuery());
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

}
