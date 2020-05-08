package nl.avisi.datasource;

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
     * @throws SQLException when the database driver isn't installed on the system
     */
    public Connection conect() throws SQLException {
        final String driverName = databaseProperties.getDriverName();
        final String connectionString = databaseProperties.getConnectionString();

        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            System.err.printf("Unable to find database driver %s\n", e.getMessage());
        }

        return DriverManager.getConnection(connectionString);
    }
}
