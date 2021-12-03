import java.io.Serializable;

class Shape implements Serializable {
    int x, y;
    int width, height;
    boolean selected;

    public Shape(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.selected = false;
    }
}