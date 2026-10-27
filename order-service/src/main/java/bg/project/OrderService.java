package bg.project;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductGateway productGateway;

    public OrderService(OrderRepository orderRepository, ProductGateway productGateway) {
        this.orderRepository = orderRepository;
        this.productGateway = productGateway;
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

        Product product = productGateway.getById(createOrderRequest.productId());

        double totalPrice = createOrderRequest.quantity() * product.price();

        return orderRepository.create(createOrderRequest.productId(), createOrderRequest.quantity(), totalPrice);
    }

}
