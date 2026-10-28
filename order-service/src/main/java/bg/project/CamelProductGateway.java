package bg.project;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.util.ObjectHelper;

import java.net.ConnectException;

@ApplicationScoped
public class CamelProductGateway implements ProductGateway{

    private final ProducerTemplate producerTemplate;

    public CamelProductGateway(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @Override
    public Product getById(Long id){
        Product product;
        try{
             product = producerTemplate.requestBodyAndHeader(
                    "direct:getProduct", null, "productId", id, Product.class);

        } catch (CamelExecutionException e) {
            HttpOperationFailedException httpError = ObjectHelper.getException(HttpOperationFailedException.class, e);
            if (httpError != null && httpError.getStatusCode() == 404) {
                throw new ProductNotFoundException("Product with ID " + id + " does not exist");
            }

            if (ObjectHelper.getException(ConnectException.class, e) != null) {
                throw new ProductServiceUnavailableException("Product service is unavailable");
            }

            throw e;
        }

        if(product == null || !id.equals(product.id()) ||product.price() == null || product.price() <= 0){
            throw new InvalidProductResponseException("Product service returned invalid data for product with ID:" + id);
        }

        return product;
    }
}
