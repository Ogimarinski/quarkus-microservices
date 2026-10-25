package bg.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private OrderRepository orderRepository;
    private ProductClient productClient;
    private OrderService orderService;

    @BeforeEach
    void setUp(){
        orderRepository = mock(OrderRepository.class);
        productClient = mock(ProductClient.class);
        orderService = new OrderService(orderRepository, productClient);
    }

    @Test
    void createOrder_calculatesTotalPrice(){

        when(productClient.getById(1L)).thenReturn(new Product(1L, "Laptop", 100.0));

        orderService.createOrder(new CreateOrderRequest(1L, 3));

        verify(orderRepository).create(1L, 3, 300.0);
    }

    @Test
    void createOrder_throwsWhenQuantityIsZero(){
        assertThrows(InvalidOrderException.class, () -> orderService.createOrder(new CreateOrderRequest(1L, 0)));

        verifyNoInteractions(productClient);
    }
}
