package com.demo.reactiveapifunctional.repository;

import com.demo.reactiveapifunctional.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    Flux<Product> findByName(String name);
}
