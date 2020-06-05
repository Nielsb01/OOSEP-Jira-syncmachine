package nl.avisi.logger;

import nl.avisi.datasource.DatabaseLoggerDAO;

import javax.inject.Inject;

public class DatabaseLogger implements ILogger {

    /**
     * Used for interacting with the database
     */
    private DatabaseLoggerDAO databaseLoggerDAO;

    @Inject
    public void setDatabaseLoggerDAO(DatabaseLoggerDAO databaseLoggerDAO) {
        this.databaseLoggerDAO = databaseLoggerDAO;
    }

    /**
     * Method calls {@link DatabaseLoggerDAO} to insert Log into database
     *
     * @param e the message of this exception will be logged in the database
     */
    @Override
    public void logToDatabase(String originClass, String originMethod, Exception e) {
        databaseLoggerDAO.insertLogIntoDatabase(originClass, originMethod, e);
    }
}
