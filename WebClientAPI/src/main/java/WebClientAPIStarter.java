import model.Product;
import model.ProductEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class WebClientAPIStarter {
    private WebClient webClient;

    WebClientAPIStarter() {
        this.webClient = WebClient.create("http://localhost:8080/products");

//        this.webClient = WebClient.builder()
//                .baseUrl("http://localhost:8080/products")
//                .build();
    }

    public static void main(String args[]) {
        WebClientAPIStarter webClientAPIStarter = new WebClientAPIStarter();

        webClientAPIStarter
                .postNewProduct()
                .thenMany(webClientAPIStarter.getAllProduct())
                .take(1)
                .flatMap(p -> webClientAPIStarter.updateProduct(p.getId(), "New Tea", 9.99))
                .flatMap(p -> webClientAPIStarter.deleteProduct(p.getId()))
                .thenMany(webClientAPIStarter.getAllProduct())
                .thenMany(webClientAPIStarter.getProductEvents())
                .subscribe(System.out::println);
    }

    private Mono<ResponseEntity<Product>> postNewProduct() {
        return webClient
                .post()
                .body(Mono.just(new Product(null, "Black Tea", 1.99)), Product.class)
                .exchangeToMono(clientResponse -> clientResponse.toEntity(Product.class))
                .doOnSuccess(o -> System.out.println("**********POST: " + o));
    }

    private Mono<Product> getAProduct(final String id) {
        return webClient
                .get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnSuccess(o -> System.out.println("************* GET: " + o));
    }

    private Flux<Product> getAllProduct() {
        return webClient
                .get()
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(o -> System.out.println("*************GET: " + o));
    }

    private Mono<Product> updateProduct(String id, String name, Double price) {
        return webClient
                .put()
                .uri("/{id}", id)
                .body(Mono.just(new Product(null, name, price)), Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnSuccess(o -> System.out.println("***********UPDATE: " + o));
    }

    private Mono<Void> deleteProduct(String id) {
        return webClient
                .delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(o -> System.out.println("********** DELETE: " + o));
    }

    private Flux<ProductEvent> getProductEvents() {
        return webClient
                .get()
                .uri("/events")
                .retrieve()
                .bodyToFlux(ProductEvent.class)
                .doOnNext(o -> System.out.println("**************GET: " + o));
    }
}
