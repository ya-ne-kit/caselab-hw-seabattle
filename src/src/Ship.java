public class Ship {
    private int x;
    private int y;
    private int n;
    private int kurs;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getN() {
        return n;
    }

    public int getKurs() {
        return kurs;
    }

    public Ship(int x, int y, int n, int kurs) {
        this.x = x;
        this.y = y;
        this.n = n;
        this.kurs = kurs;
    }
}