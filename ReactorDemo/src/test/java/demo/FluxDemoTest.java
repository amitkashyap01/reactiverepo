package demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;

public class FluxDemoTest {

    @Test
    public void firstFlux(){
        Flux.just("A", "B", "C")
                .log()
                .subscribe();
    }

    @Test
    public void fluxFromIteraable(){
        Flux.fromIterable(Arrays.asList("A", "B", "C"))
                .log()
                .subscribe();
    }

    @Test
    public void fluxFromRange(){
        Flux.range(10, 5)
                .log()
                .subscribe();
    }
}
