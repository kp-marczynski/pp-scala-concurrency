package zad1i2;

public class MonitoredCount extends Count {
    @Override
    public void run() {
        int temp;
        for (int i = 0; i < 200000; i++) {
            synchronized (n) {
                temp = n.getN();
                n.setN(temp + 1);
            }
        }
    }
}
