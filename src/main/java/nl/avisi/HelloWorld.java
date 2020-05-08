package nl.avisi;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("test")
public class HelloWorld {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return "Hello world";
    }
}
