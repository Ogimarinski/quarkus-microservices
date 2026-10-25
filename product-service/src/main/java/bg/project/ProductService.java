package bg.project;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
    }

    public Product createProduct(Product product){
        return productRepository.create(product);
    }

    public Optional<Product> updateProduct(Long id, Product product){
        return productRepository.update(id, product);
    }

    public boolean deleteProduct(Long id){
        return productRepository.delete(id);
    }
}
