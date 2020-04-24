package nl.avisi;

import kong.unirest.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("data")
public class RetrieveData {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonNode retrieveAllWorklogs() {


        DateDTO dateDTO = new DateDTO("2020-04-01", "2020-04-25");

        HttpResponse<JsonNode> worklogs = Unirest.post("http://127.0.0.1/rest/tempo-timesheets/4/worklogs/search")
                .basicAuth("ruubz2", "xtkWMeAbZcWB6FN")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(dateDTO)
                .asJson();

            int[] seconds = new int[worklogs.getBody().getArray().length()];

        for (int i = 0; i < worklogs.getBody().getArray().length(); i++) {
            System.out.println(worklogs.getBody().getArray().getJSONObject(i).get("timeSpentSeconds"));

        }
        return worklogs.getBody();
    }


    //@Produces(MediaType.APPLICATION_JSON)
    public void retrieveData() {

        String response = Unirest.get("http://127.0.0.1/rest/api/2/issue/OG-10")
                .basicAuth("ruubz2", "xtkWMeAbZcWB6FN")
                .header("Accept", "application/json")
                .asJson()
                .getBody()
                .getObject()
                .getJSONObject("fields")
                .get("timespent")
                .toString();

        System.out.println(response);

       /* System.out.println(response.getBody());

        return Response.status(200).entity(response.getBody()).build();*/
    }

    private String loginToJira() {
        ObjectMapper mapper = new JsonObjectMapper();


        HttpResponse<String> response = Unirest.get("https://raw.githubusercontent.com/HANICA-DEA/exercise-http-client/solution/README.md")
                .header("Accept", "application/json")
                .asString();

        return response.getBody();
    }

}
