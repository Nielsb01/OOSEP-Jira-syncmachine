package nl.avisi;

import kong.unirest.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("data")
public class RetrieveData {



    @GET
    public String retrieveAllIssuesFromProject() {
        HttpResponse<JsonNode> worklogs = Unirest.get("http://127.0.0.1/core/3/worklogs?from=2020-04-01&to=2020-04-24")
                .basicAuth("ruubz2", "xtkWMeAbZcWB6FN")
                .header("Accept", "application/json")
                .asJson();


        System.out.println(worklogs.getBody());
        return worklogs.getBody().toString();
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

 /*   public String loginToJira() {
        String output = null;
        StringBuilder loginResponse = new StringBuilder();

        try {
            URL url = new URL("http://127.0.0.1/rest/api/2/auth/1/session");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "application/json");

           String input = "{\"username\":\"" + "ruubz2" + "\", \"password\":\"" + "xtkWMeAbZcWB6FN" + "\"}";

            OutputStream os = connection.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if(connection.getResponseCode() == 200) {
               BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
               while((output = br.readLine()) != null) {
                    loginResponse.append(output);
                }
               connection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loginResponse.toString();
    }*/

}
