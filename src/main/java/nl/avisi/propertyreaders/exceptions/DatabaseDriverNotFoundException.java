package nl.avisi.propertyreaders.exceptions;

public class DatabaseDriverNotFoundException extends RuntimeException {
    private String driverName;

    public DatabaseDriverNotFoundException(String driverName) {
        this.driverName = driverName;
    }

    @Override
    public String getMessage() {
        return this.driverName;
    }
}
