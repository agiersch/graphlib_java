import java.util.*;

public class Exemple2 {

    public static void main(String[] args) {
        // Création de la fenêtre
        DrawingWindow w = new DrawingWindow("Exemple 2", 640, 480);

        int width = Math.min(w.width - 1, w.height - 1) / 2;

        // Affichage de rectangles concentriques, avec un dégradé de
        // couleurs
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
            // On change la couleur de dessin...
            w.setColor(r, g, b);
            // ... et on affiche un rectangle
            w.drawRect(z, z, w.width - 1 - z, w.height - 1 - z);
        }
    }
}
