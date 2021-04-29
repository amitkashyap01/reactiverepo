package com.demo.reactiveapi;

import com.demo.reactiveapi.model.Product;
import com.demo.reactiveapi.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ReactiveapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveapiApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ProductRepository productRepository){
			return args -> {
				Flux<Product> fluxOfProducts = Flux.just(
						new Product(null, "Big Latte", 2.99),
						new Product(null, "Big Decaf", 2.49),
						new Product(null, "Green Tea", 1.99))
						.flatMap(productRepository::save);

				fluxOfProducts
						.thenMany(productRepository.findAll())
						.subscribe(System.out::println);
			};
	}
}
