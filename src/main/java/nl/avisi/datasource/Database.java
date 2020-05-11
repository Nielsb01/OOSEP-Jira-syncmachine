package nl.avisi.datasource;

import nl.avisi.propertyreaders.exceptions.DatabaseDriverNotFoundException;
import nl.avisi.propertyreaders.DatabaseProperties;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    /**
     * The class which contains the database
     * driver and connection information
     */
    private DatabaseProperties databaseProperties;

    /**
     * Inject the database properties dependency
     *
     * @param databaseProperties database properties file
     */
    @Inject
    public void setDatabaseProperties(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    /**
     * Open the database connection based on the database
     * driver set in the properties file
     *
     * @return a new connection to the database
     * @throws DatabaseDriverNotFoundException when the database driver isn't installed on the system
     * @throws SQLException when no connection to the database could be established
     */
    public Connection connect() throws SQLException, DatabaseDriverNotFoundException {
        this.databaseProperties.loadPropertyFile();

        final String driverName = databaseProperties.getDriverName();
        final String connectionString = databaseProperties.getConnectionString();

        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            throw new DatabaseDriverNotFoundException(e.getMessage());
        }

        return DriverManager.getConnection(connectionString);
    }
}
