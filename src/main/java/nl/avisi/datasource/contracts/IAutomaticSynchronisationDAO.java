package nl.avisi.datasource.contracts;

public interface IAutomaticSynchronisationDAO {
    String getLastSynchronisationMoment();
    void setLastSynchronisationMoment(String newLastSynchronisationDate);
}
