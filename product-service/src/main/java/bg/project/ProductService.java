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

        validate(product);
        return productRepository.create(product);
    }

    public Optional<Product> updateProduct(Long id, Product product){
        validate(product);
        return productRepository.update(id, product);
    }

    public boolean deleteProduct(Long id){
        return productRepository.delete(id);
    }

    private void validate(Product product){
        if(product == null){
            throw new InvalidProductException("Request body is required");
        }

        if(product.name() == null || product.name().isBlank()){
            throw new InvalidProductException("Name is required");
        }

        if(product.name().length() > 100){
            throw new InvalidProductException("Name must be at most 100 chars");
        }

        if(product.price() == null){
            throw new InvalidProductException("Price is required");
        }

        if(product.price() <= 0){
            throw new InvalidProductException("Price must be greater than 0");
        }
    }
}
