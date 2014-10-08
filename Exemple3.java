import java.util.*;

public class Exemple3 {

    static final Random random = new Random();

    public static void main(String[] args) {
        DrawingWindow w = new DrawingWindow("Exemple 3", 640, 480);

        for (int i = 0; ; i++) {

            int x1 = random.nextInt(w.width);
            int y1 = random.nextInt(w.height);
            int x2 = random.nextInt(w.width);
            int y2 = random.nextInt(w.height);
            w.setColor(random.nextFloat(),
                       random.nextFloat(),
                       random.nextFloat());
            w.drawLine(x1, y1, x2, y2);
            w.sync();
        }
    }
}

