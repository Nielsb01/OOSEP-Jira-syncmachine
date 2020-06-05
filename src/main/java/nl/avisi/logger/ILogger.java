package nl.avisi.logger;

public interface ILogger {
    void logToDatabase(String name, Exception e);
}
