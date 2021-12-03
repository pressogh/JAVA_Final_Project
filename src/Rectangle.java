import java.awt.*;

class Rectangle extends Shape {
    public Rectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
    public void draw(Graphics g) {
        g.drawRect(x, y, width, height);
        if (selected) {
            g.drawRect(x - 2, y - 2, 4, 4);
            g.drawRect(x + width - 2, y + height - 2, 4, 4);
        }
    }
}