
package nl.avisi.datasource.contracts;

import nl.avisi.dto.UserDTO;

public interface ILoginDAO {
    UserDTO getLoginInfo(String username);
}
