import java.util.*;

public class Exemple2 {

    public static void main(String[] args) {
        DrawingWindow w = new DrawingWindow("Exemple 3", 640, 480);

        int width = Math.min(w.width - 1, w.height - 1) / 2;

        for (int z = 0; z <= width; z++) {
            float r, g, b;
            float s = 3.0f * z / width;
            if (z <= width / 3.0f) {
                r = 1.0f - s;
                g = s;
                b = 0.0f;
            } else if (z <= 2.0f * width / 3.0f) {
                s -= 1.0f;
                r = 0.0f;
                g = 1.0f - s;
                b = s;
            } else {
                s -= 2.0f;
                r = s;
                g = 0.0f;
                b = 1.0f - s;
            }
            w.setColor(r, g, b);
            w.drawRect(z, z, w.width - 1 - z, w.height - 1 - z);
        }
    }
}
