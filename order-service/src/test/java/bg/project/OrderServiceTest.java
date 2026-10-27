package bg.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private OrderRepository orderRepository;
    private ProductGateway productGateway;
    private OrderService orderService;

    @BeforeEach
    void setUp(){
        orderRepository = mock(OrderRepository.class);
        productGateway = mock(ProductGateway.class);
        orderService = new OrderService(orderRepository, productGateway);
    }

    @Test
    void createOrder_calculatesTotalPrice(){

        when(productGateway.getById(1L)).thenReturn(new Product(1L, "Laptop", 100.0));

        orderService.createOrder(new CreateOrderRequest(1L, 3));

        verify(orderRepository).create(1L, 3, 300.0);
    }

    @Test
    void createOrder_throwsWhenQuantityIsZero(){
        assertThrows(InvalidOrderException.class, () -> orderService.createOrder(new CreateOrderRequest(1L, 0)));

        verifyNoInteractions(productGateway);
    }
}
