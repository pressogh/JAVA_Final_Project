import java.awt.*;

class Line extends Shape {
    public Line(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }
    public void draw(Graphics g) {
        g.drawLine(x, y, width, height);
        if (selected) {
            g.drawRect(x - 2, y - 2, 4, 4);
            g.drawRect(width - 2,height - 2, 4, 4);
        }
    }
}