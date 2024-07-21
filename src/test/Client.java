package test;

import com.speedyman77.framework.Server;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client extends Assert {
    HttpClient client = HttpClient.newBuilder().build();
    @Test
    public void HelloWorldTest() throws URISyntaxException, IOException, InterruptedException {
        Server server = new Server();

        server.init();

        server.awaitUntilReady();

        HttpRequest req = HttpRequest.newBuilder(new URI("http://localhost"))
                .GET()
                .build();
        HttpResponse<String> res = null;
        try {
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to connect to localhost");
        }

        server.close();

        assertEquals(res.body(), "Hello World");
    }
}
