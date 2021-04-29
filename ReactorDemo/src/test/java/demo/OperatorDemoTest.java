package demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class OperatorDemoTest {

    @Test
    public void map(){
        Flux.range(1, 5)
                .map(i -> i *10)
                .subscribe(System.out::println);
    }

    @Test
    public void flatMap(){ //same like map, it also transform but it do it in asynchronous way
        Flux.range(1, 5)
                .flatMap(i -> Flux.range(i*10, 2))
                .subscribe(System.out::println);
    }
}
