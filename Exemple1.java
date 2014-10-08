public class Exemple1 {

    public static void main(String[] args) {
        DrawingWindow w = new DrawingWindow("Exemple 1", 640, 480);

        final int cx = w.width / 2;
        final int cy = w.height / 2;
        final int delta = 5;
        for (int x = 0; x < w.width; x += delta) {
            w.drawLine(cx, cy, x, 0);
            w.drawLine(cx, cy, w.width - 1 - x, w.height - 1);
        }
        for (int y = 0; y < w.height; y += delta) {
            w.drawLine(cx, cy, 0, w.height - 1 - y);
            w.drawLine(cx, cy, w.width - 1, y);
        }

    }
}
