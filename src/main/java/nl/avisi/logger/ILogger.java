package nl.avisi.logger;

public interface ILogger {
    void logToDatabase(String originClass, String originMethod, Exception e);
}
