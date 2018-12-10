package zad1i2;

public class Main {
    public static void main(String[] args) {
        runCount(new Count(),new Count());
        Count.clearN();
        runCount(new MonitoredCount(),new MonitoredCount());
        Count.clearN();
        runCount(new SemaphoredCount(),new SemaphoredCount());

    }

    public static void runCount(Count p, Count q) {
        p.start();
        q.start();
        try {
            p.join();
            q.join();
        } catch (InterruptedException e) {
        }
        System.out.println(p.getClass().getSimpleName() + ": The value of n is " + Count.getN());
    }
}
