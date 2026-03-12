package ca.pitt.demo.idp.skupperfrontend.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Path("/hello")
public class Welcome {

    @ConfigProperty(name = "downstream.server")
    String downstreamServer;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getServer() {
        try {
            String url = downstreamServer;
            if (url.startsWith("\"") && url.endsWith("\"")) {
                url = url.substring(1, url.length() - 1);
            }
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/server"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}