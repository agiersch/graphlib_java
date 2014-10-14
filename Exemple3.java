import java.util.*;

class Exemple3 {

    static final Random random = new Random();

    public static void main(String[] args) {
        // Création de la fenêtre
        DrawingWindow w = new DrawingWindow("Exemple 3", 640, 480);

        // Sans s'arrêter, affiche des lignes au hasard avec une
        // couleur tirée aléatoirement
        while (true) {
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
