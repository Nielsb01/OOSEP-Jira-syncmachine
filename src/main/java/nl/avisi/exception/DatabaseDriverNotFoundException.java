package nl.avisi.exception;

public class DatabaseDriverNotFoundException extends Exception {
    private String driverName;

    public DatabaseDriverNotFoundException(String driverName) {
        this.driverName = driverName;
    }

    @Override
    public String getMessage() {
        return this.driverName;
    }
}
