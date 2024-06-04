public class Platidlo {
    private String typ;
    private int hodnota;
    private int pocet;

    public Platidlo(String typ, int hodnota) {
        this.typ = typ;
        this.hodnota = hodnota;
        this.pocet = 0;
    }

    public String getTyp() {
        return typ;
    }

    public int getHodnota() {
        return hodnota;
    }

    public int getPocet() {
        return pocet;
    }

    public void setPocet(int pocet) {
        this.pocet = pocet;
    }
}