package zad4;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Feast {
    private final List<Semaphore> chopsticks;
    private final List<Philosopher> philosophers;

    public Feast(int N) {
        Philosopher.doorman = new Semaphore(N - 1);
        chopsticks = Collections.nCopies(N, new Semaphore(1, true));
        philosophers = IntStream.range(0, N).boxed()
                .map(id -> new Philosopher(id, chopsticks.get(id), chopsticks.get((id + 1) % 4)))
                .collect(Collectors.toList());
    }

    private void startFeast() {

        System.out.println("Start dining!");
        //Start philosophers
        philosophers.forEach(Thread::start);

        // Wait for them to finish
        philosophers.forEach(philosopher->{
            try{
                philosopher.join();
            } catch (InterruptedException ignored){}
        });

        System.out.println("Done eating.");
    }

    /**
     * Dining simulations - puts philosophers by the table with their chopsticks
     */
    public static void main(String[] args) {
        new Feast(5).startFeast();            // five philosophers, five chopsticks
    }
}