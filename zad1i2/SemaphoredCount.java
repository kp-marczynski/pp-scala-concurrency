package zad1i2;

import java.util.concurrent.Semaphore;

public class SemaphoredCount extends Count {
    private static Semaphore semaphore = new Semaphore(1);
    @Override
    public void run() {
        int temp;
        for (int i = 0; i < 200000; i++) {
            try {
                semaphore.acquire();
                temp = n.getN();
                n.setN(temp + 1);
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
