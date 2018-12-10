package zad4short;

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
                .map(id -> new Philosopher(chopsticks.get(id), chopsticks.get((id + 1) % N)))
                .collect(Collectors.toList());
    }

    private void startFeast() {
        philosophers.forEach(Thread::start);
        philosophers.forEach(philosopher->{
            try{
                philosopher.join();
            } catch (InterruptedException ignored){}
        });
    }

    public static void main(String[] args) {
        new Feast(5).startFeast();
    }
}