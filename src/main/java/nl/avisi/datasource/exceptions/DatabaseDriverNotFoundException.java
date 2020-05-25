package nl.avisi.datasource.exceptions;

public class DatabaseDriverNotFoundException extends RuntimeException {
    private final String DRIVER_NAME;

    public DatabaseDriverNotFoundException(String driverName) {
        this.DRIVER_NAME = driverName;
    }

    @Override
    public String getMessage() {
        return this.DRIVER_NAME;
    }
}
