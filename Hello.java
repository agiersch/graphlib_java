public class Hello {

    public static void main(String[] args) {
        // Création d'une fenêtre avec, en paramètres, son titre, sa
        // largeur et sa hauteur
        DrawingWindow w = new DrawingWindow("Exemple 1", 640, 480);

        // Affichage d'un court message au milieu de la fenêtre
        w.drawText(w.width / 2, w.height / 2, "Hello world!");
    }
}
