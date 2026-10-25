package bg.project;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "product-api")
@Path("/products")
public interface ProductClient {

    @GET
    @Path("/{id}")
    Product getById(@PathParam("id") Long id);
}
