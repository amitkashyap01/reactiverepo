package demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class MonoDemoTest {


    @Test
    public void firstMono(){
        Mono.just("Amit")
            .log()
            .subscribe();
    }

    @Test
    public void MonoWithCOnsumer(){
        Mono.just("Amit")
                .log()
                .subscribe(s -> System.out.println(s));
    }

    @Test
    public void monoWithDoOn(){
        Mono.just("Amit")
                .log()
                .doOnSubscribe(subs -> System.out.println("Subscribed: "+subs))
                .doOnRequest(request -> System.out.println("Request: "+request))
                .doOnSuccess(complete -> System.out.println("Complete: "+complete))
                .subscribe(System.out::println);
    }
}
