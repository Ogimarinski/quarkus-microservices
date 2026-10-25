package bg.project;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GET
    public List<Product> getProducts(){
        return productService.getProducts();
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id){
        return productService.getProductById(id)
                .map(product -> Response.ok(product).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(Product product){
        Product createdProduct = productService.createProduct(product);

        return Response.status(Response.Status.CREATED)
                .entity(createdProduct)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProduct(@PathParam("id") Long id, Product product){
        return productService.updateProduct(id, product)
                .map(updatedProduct -> Response.ok(updatedProduct).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id){
        boolean isDeleteCompleted = productService.deleteProduct(id);

        if(isDeleteCompleted){
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}