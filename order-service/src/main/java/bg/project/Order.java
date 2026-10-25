package bg.project;

public record Order(Long id, Long productId, int quantity, double totalPrice) {
}
