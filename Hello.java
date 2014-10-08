public class Hello {

    public static void main(String[] args) {
        DrawingWindow w = new DrawingWindow("Exemple 1", 640, 480);

        w.drawText(w.width / 2, w.height / 2, "Hello world!");
    }

}