package nl.avisi.datasource;

import nl.avisi.datasource.database.Database;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutomaticSynchronisationDAO {

    private Database database;

    @Inject
    public void setDatabase(Database database) {
        this.database = database;
    }

    private static final String GET_LAST_SYNCHRONISATION_DATE_SQL = "SELECT synchronisation_date FROM automatic_synchronisation ORDER BY synchronisation_date DESC LIMIT 1";
    private static final String SET_LAST_SYNCHRONISATION_DAT_SQL = "INSERT INTO automatic_synchronisation (synchronisation_date) VALUES (?)";

    /**
     * fetches the last datetime on which an automatic synchronisation occurred
     * @return The last datetime on which an automatic synchronisation occurred in YYYY-MM-DD HH-MM-SS
     */
    public String getLastSynchronisationDate() {

        String lastSynchronisationDate;

        try (Connection connection = database.connect();
             PreparedStatement stmt = connection.prepareStatement(GET_LAST_SYNCHRONISATION_DATE_SQL)) {

            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();

            lastSynchronisationDate = resultSet.getString("synchronisation_date");

        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        return lastSynchronisationDate;

    }

    /**
     * adds a datetime to the database on which an automatic synchronisation occurred
     * @param newLastSynchronisationDate the datetime on which the automatic synchronisation occurred in YYYY-MM-DD HH-MM-SS
     */
    public void setLastSynchronisationDate(String newLastSynchronisationDate) {

        try (Connection connection = database.connect();
             PreparedStatement stmt = connection.prepareStatement(SET_LAST_SYNCHRONISATION_DAT_SQL)) {
            stmt.setString(1, newLastSynchronisationDate);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerErrorException(String.format("Error occurred while updating the auto synchronisation status: %s", e.getMessage()));
        }

    }
}
