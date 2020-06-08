package nl.avisi.datasource;

import nl.avisi.datasource.contracts.IAutomaticSynchronisationDAO;
import nl.avisi.datasource.database.Database;
import nl.avisi.datasource.exceptions.LastSynchronisationDateNotFoundException;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutomaticSynchronisationDAO implements IAutomaticSynchronisationDAO {

    private Database database;

    @Inject
    public void setDatabase(Database database) {
        this.database = database;
    }

    private static final String GET_LAST_SYNCHRONISATION_DATE_SQL = "SELECT synchronisation_moment FROM automatic_synchronisation ORDER BY synchronisation_moment DESC LIMIT 1";
    private static final String SET_LAST_SYNCHRONISATION_DAT_SQL = "INSERT INTO automatic_synchronisation (synchronisation_moment) VALUES (?)";

    /**
     * fetches the last datetime on which an automatic synchronisation occurred
     * @return The last datetime on which an automatic synchronisation occurred in YYYY-MM-DD HH-MM-SS
     */
    public String getLastSynchronisationMoment() {

        String lastSynchronisationDate;

        try (Connection connection = database.connect();
             PreparedStatement stmt = connection.prepareStatement(GET_LAST_SYNCHRONISATION_DATE_SQL)) {

            ResultSet resultSet = stmt.executeQuery();
            boolean resultStatus = resultSet.next();

            if (!resultStatus) {
                throw new LastSynchronisationDateNotFoundException();
            }

            lastSynchronisationDate = resultSet.getString("synchronisation_moment");

        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        return lastSynchronisationDate;

    }

    /**
     * adds a datetime to the database on which an automatic synchronisation occurred
     * @param newLastSynchronisationDate the datetime on which the automatic synchronisation occurred in YYYY-MM-DD HH-MM-SS
     */
    public void setLastSynchronisationMoment(String newLastSynchronisationDate) {

        try (Connection connection = database.connect();
             PreparedStatement stmt = connection.prepareStatement(SET_LAST_SYNCHRONISATION_DAT_SQL)) {
            stmt.setString(1, newLastSynchronisationDate);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerErrorException(String.format("Error occurred while updating the last synchronisation date: %s", e.getMessage()));
        }

    }
}
