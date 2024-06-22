import java.util.Random;
public class Shape {
    private static final int[][][] shapes = {
        {{0, 1}, {1, 1}, {2, 1}, {3, 1}}, // I
        {{0, 0}, {0, 1}, {1, 1}, {2, 1}},  // J
        {{0, 1}, {1, 1}, {2, 1}, {2, 0}},  // L
        {{0, 0}, {0, 1}, {1, 0}, {1, 1}},  // O
        {{1, 0}, {2, 0}, {0, 1}, {1, 1}},  // S
        {{1, 0}, {0, 1}, {1, 1}, {2, 1}},  // T
        {{0, 0}, {1, 0}, {1, 1}, {2, 1}}   // Z
    };
    private int[][] coords;

    public Shape() {
        this.coords = shapes[new Random().nextInt(shapes.length)];
    }

    public int[][] getCoordinates() {
        return this.coords;
    }
}
