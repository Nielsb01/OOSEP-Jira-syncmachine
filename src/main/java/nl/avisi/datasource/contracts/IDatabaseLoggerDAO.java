package nl.avisi.datasource.contracts;

public interface IDatabaseLoggerDAO {
    void insertLogIntoDatabase(String className, String methodName, String errorToLog);
}
