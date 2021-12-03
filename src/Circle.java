import java.awt.*;

class Circle extends Shape {
    public Circle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
    public void draw(Graphics g) {
        g.drawOval(x, y, width, height);
        if (selected) {
            g.drawRect(x - 2, y - 2, 4, 4);
            g.drawRect(x + width - 2, y + height - 2, 4, 4);
        }
    }
}