package nl.avisi;

import nl.avisi.http.IRequest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("test")
public class HelloWorld {
    @Inject
    private IRequest request;

//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public String helloWorld() {
//        return "Hello world";
//    }


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Object test() {
        return request.get("http://localhost:3000/test.php");
    }

}
