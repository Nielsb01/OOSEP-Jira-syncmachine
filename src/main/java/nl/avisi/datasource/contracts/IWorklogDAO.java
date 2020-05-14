package nl.avisi.datasource.contracts;

import java.util.List;

public interface IWorklogDAO {
    List<Integer> getAllWorklogIds();
    void addWorklogId(int worklogId);
}
