package bg.project;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @GET
    public List<Order> getOrders(){
        return orderService.getAllOrders();
    }

    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") Long id){
        return orderService.findById(id)
                .map(product -> Response.ok(product).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crateProduct(CreateOrderRequest createOrderRequest){
        Order createdOrder = orderService.createOrder(createOrderRequest);

        return Response.status(Response.Status.CREATED)
                .entity(createdOrder)
                .build();
    }
}
