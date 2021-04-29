package com.demo.reactiveapifunctional.handler;

import com.demo.reactiveapifunctional.model.Product;
import com.demo.reactiveapifunctional.model.ProductEvent;
import com.demo.reactiveapifunctional.repository.ProductRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mongodb.internal.connection.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ProductHandler {

    @Autowired
    ProductRepository productRepository;

    public Mono<ServerResponse> getAllProducts(ServerRequest request) {
        Flux<Product> products = productRepository.findAll();


        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(products, Product.class);
    }

    public Mono<ServerResponse> getProduct(ServerRequest serverRequest) {
        final String id = serverRequest.pathVariable("id");
        final Mono<Product> productMono = this.productRepository.findById(id);
        final Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return productMono
                .flatMap(product ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(product, Product.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> saveProduct(ServerRequest serverRequest) {

        final Mono<Product> productMono = serverRequest.bodyToMono(Product.class);

        return productMono
                .flatMap(product -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productRepository.save(product), Product.class));
    }


    public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {

        final String id = serverRequest.pathVariable("id");
        final Mono<Product> productMono = serverRequest.bodyToMono(Product.class);
        final Mono<Product> existingProductMono = productRepository.findById(id);

        final Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return productMono.zipWith(existingProductMono,
                (product, existingProduct) ->
                        new Product(existingProduct.getId(), product.getName(), product.getPrice())
        ).flatMap(product -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productRepository.save(product), Product.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest){
        final String id = serverRequest.pathVariable("id");

        Mono<Product> productMono = this.productRepository.findById(id);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return productMono
                .flatMap(product -> ServerResponse.ok()
                            .build(productRepository.delete(product)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteAllProducts(ServerRequest serverRequest){
        return ServerResponse.ok()
                .build(productRepository.deleteAll());
    }


    public Mono<ServerResponse> getProductEvents(ServerRequest serverRequest){

        Flux<ProductEvent> eventFlux = Flux.interval(Duration.ofSeconds(1))
                .map(val -> new ProductEvent(val, "Product Event"));

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(eventFlux, ProductEvent.class);
    }
}
