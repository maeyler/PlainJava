import java.awt.Point;
class Color implements Comparable<Color>  {
    int red, green, blue;
    public Color(int r, int g, int b) {
        red = r; green = g; blue = b;
    }
    public int intensity() { return red+green+blue; }
    public int compareTo(Color c) {
        return intensity() - c.intensity();
    }
}
class ColorPoint extends Point {
    Color col;
    public ColorPoint(int x, int y, Color c) {
        super(x, y); col = c;
    }
}
class Final  {
    static Color pink = new Color(255, 175, 175);
    static Point p1 = new ColorPoint(80, 50, pink);
    static Point p2 = p1;
    public static void main(String[] args) {
        System.out.println(p2);
        System.out.println(pink.compareTo(pink));
    }
}

