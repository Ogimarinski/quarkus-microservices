package bg.project;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.net.ConnectException;

@ApplicationScoped
public class ProductRoute extends RouteBuilder {

    @Override
    public void configure() {
        onException(ConnectException.class)
                .maximumRedeliveries(3)
                .redeliveryDelay(500);

        from("direct:getProduct")
                .routeId("get-product")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .toD("{{product-service.url}}/products/${header.productId}")
                .unmarshal().json(JsonLibrary.Jackson, Product.class);
    }
}