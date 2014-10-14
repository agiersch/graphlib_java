class Test{
    public static void main(String[] args) {
        DrawingWindow w;

        w = new DrawingWindow("Test!", 400, 400);

        w.setColor("lawngreen");
        for (int i = 0; i < 12; i++) {
            int p = 10 * i + 10;
            w.drawLine(p, 0, p, 175);
            w.drawLine(p + i, 0, p + i, 175);
        }

        w.setColor("black");
        for (int i = 0; i < 12; i++) {
            int p = 10 * i + 10;

            w.drawCircle(p, 25, i);
            w.fillCircle(p, 50, i);

            w.drawRect(p, 75, p + i, 75 + i);
            w.fillRect(p, 100, p + i, 100 + i);

            w.drawTriangle(p, 125, p + i, 125 + i/2, p, 125 + i);
            w.fillTriangle(p, 150, p + i, 150 + i/2, p, 150 + i);
        }

        w = new DrawingWindow("Test!", 800, 600);

        w.setBgColor("red");
        w.setColor("blue");
        for (int i = 0; i < 10; i++) {
            w.clearGraph();
            for (int y = 0; y < w.height; y++) {
                for (int x = 0; x < w.width; x++) {
                    w.drawPoint(x, y);
                }
            }
        }
        w.setColor("white");
        for (int i = 0; i < 10; i++) {
            w.clearGraph();
            for (int y = 0; y < w.height; y++) {
                for (int x = 0; x < w.width; x++) {
                    w.drawPoint(x, y);
                }
                w.sync();
            }
        }
        w.closeGraph();
    }
}
