class Test{
    public static void main(String[] args) {
        DrawingWindow w1 = new DrawingWindow("Test!", 400, 400);
        w1.setColor("lawngreen");
        for (int i = 0; i < 12; i++) {
            int p = 10 * i + 10;
            w1.drawLine(p, 0, p, 175);
            w1.drawLine(p + i, 0, p + i, 175);
        }

        w1.setColor("black");
        for (int i = 0; i < 12; i++) {
            int p = 10 * i + 10;

            w1.drawCircle(p, 25, i);
            w1.fillCircle(p, 50, i);

            w1.drawRect(p, 75, p + i, 75 + i);
            w1.fillRect(p, 100, p + i, 100 + i);

            w1.drawTriangle(p, 125, p + i, 125 + i/2, p, 125 + i);
            w1.fillTriangle(p, 150, p + i, 150 + i/2, p, 150 + i);
        }

        DrawingWindow w2 = new DrawingWindow("Test!", 800, 600);
        w2.setBgColor("red");
        w2.setColor("blue");
        for (int i = 0; i < 3; i++) {
            w2.clearGraph();
            for (int y = 0; y < w2.height; y++) {
                for (int x = 0; x < w2.width; x++) {
                    w2.drawPoint(x, y);
                }
            }
        }
        w2.setColor("white");
        for (int i = 0; i < 3; i++) {
            w2.clearGraph();
            for (int y = 0; y < w2.height; y++) {
                for (int x = 0; x < w2.width; x++) {
                    w2.drawPoint(x, y);
                }
                w2.sync();
            }
        }
        w2.closeGraph();

        System.out.println("Click anywhere on w1...");

        while (w1.waitMousePress(5 * 1000)) {
            int x = w1.getMouseX();
            int y = w1.getMouseY();
            System.out.println("[ " + x + " ; " + y + " ] - " +
                               w1.getMouseButton());
            w1.drawLine(x - 5, y, x + 5, y);
            w1.drawLine(x, y - 5, x, y + 5);
        }

        System.out.println("Done!");
    }
}
