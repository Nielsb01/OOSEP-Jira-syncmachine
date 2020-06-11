package nl.avisi.model.exceptions.mappers;

import nl.avisi.model.exceptions.InvalidUsernameException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidUsernameExceptionMapper implements ExceptionMapper<InvalidUsernameException> {

    @Override
    public Response toResponse(InvalidUsernameException ex) {
        return Response.status(400)
                .entity("InvalidUsernameException: invalid e-mails provided")
                .build();
    }
}
