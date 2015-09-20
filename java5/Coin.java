public enum Coin {
    penny(1, Color.copper), 
    nickel(5, Color.nickel), 
    dime(10, Color.silver), 
    quarter(25, Color.silver);
    
    enum Color { copper, nickel, silver }

    final int cents; final Color color;
    Coin(int v, Color c) { cents = v; color = c; }
    public int cents() { return cents; }
    public Color color() { return color; }

    public static void main(String... args) {
        for (Coin c : Coin.values())
            System.out.printf("%2sc  %s/%s %n", c.cents(), c, c.color());
    }
}
