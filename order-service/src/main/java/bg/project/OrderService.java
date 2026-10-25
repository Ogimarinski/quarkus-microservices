package bg.project;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderService(OrderRepository orderRepository, @RestClient ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id){
        return orderRepository.findById(id);
    }

    public Order createOrder(CreateOrderRequest createOrderRequest){

        if(createOrderRequest.productId() == null){
            throw new InvalidOrderException("Product ID must be greater than 0");
        }

        if(createOrderRequest.quantity() <= 0){
            throw new InvalidOrderException("Quantity must be greater than 0");
        }

        Product product;
        try{
            product = productClient.getById(createOrderRequest.productId());
        }catch (WebApplicationException e){
            if(e.getResponse().getStatus() == 404){
                throw new ProductNotFoundException("Product with ID" + createOrderRequest.productId() + " does not exist");
            }

            throw e;
        } catch (ProcessingException e){
            throw new ProductServiceUnavailableException("Product service is unavailable");
        }

        double totalPrice = createOrderRequest.quantity() * product.price();

        return orderRepository.create(createOrderRequest.productId(), createOrderRequest.quantity(), totalPrice);
    }

}
