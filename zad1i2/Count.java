package zad1i2;

class IntCell {
    private int n = 0;
    public int getN() {return n;}
    public void setN(int n) {this.n = n;}
}

public class Count extends Thread {
    static IntCell n = new IntCell();

    public void run() {
        int temp;
        for (int i = 0; i < 200000; i++) {
            temp = n.getN();
            n.setN(temp + 1);
        }
    }

    public static int getN(){
        return n.getN();
    }

    public static void clearN(){
        n.setN(0);
    }
}