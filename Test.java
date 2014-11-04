class Test{
    public static void main(String[] args) {
        DrawingWindow w1 = new DrawingWindow("Test!", 400, 300);
        final int dy = 25;

        w1.setColor("lawngreen");
        for (int i = 0; i < 12; i++) {
            int p = 10 * i + 10;
            w1.drawLine(p, 0, p, w1.height - 1);
            w1.drawLine(p + i, 0, p + i, w1.height - 1);
        }

        w1.setColor("black");
        for (int i = 0; i < 12; i++) {
            int p = 10 * i + 10;
            int y = 0;

            y += dy;
            w1.drawText(150, y + 5, "circles");
            w1.drawCircle(p, y, i);
            y += dy;
            w1.drawText(150, y + 5, "filled circles");
            w1.fillCircle(p, y, i);

            y += dy;
            w1.drawText(150, y + 10, "rectangles with lines");
            w1.drawLine(p, y, p, y + i);
            w1.drawLine(p, y + i, p + i, y + i);
            w1.drawLine(p + i, y + i, p + i, y);
            w1.drawLine(p + i, y, p, y);

            y += dy;
            w1.drawText(150, y + 10, "rectangles");
            w1.drawRect(p, y, p + i, y + i);
            y += dy;
            w1.drawText(150, y + 10, "filled rectangles");
            w1.fillRect(p, y, p + i, y + i);

            y += dy;
            w1.drawText(150, y + 10, "triangles with lines");
            w1.drawLine(p, y, p + i, y + i/2);
            w1.drawLine(p + i, y + i/2, p, y + i);
            w1.drawLine(p, y + i, p, y);

            y += dy;
            w1.drawText(150, y + 10, "triangles");
            w1.drawTriangle(p, y, p + i, y + i/2, p, y + i);
            y += dy;
            w1.drawText(150, y + 10, "filled triangles");
            w1.fillTriangle(p, y, p + i, y + i/2, p, y + i);

        }

        // Try out of bounds drawings
        w1.setColor("blue");
        w1.drawLine(-10, w1.height - 10, w1.width + 10, w1.height - 10);
        w1.drawLine(w1.width - 10, -10, w1.width - 10, w1.height + 10);
        w1.setColor("red");
        for (int x = -10; x <= w1.width + 10; x++) {
            int y = w1.height - 20;
            w1.drawPoint(x, y);
            int c = w1.getPointColor(x, y);
            if (c != (x < 0 || x >= w1.width ? 0 : 0x00ff0000))
                throw new AssertionError("Error with getPointColor(): " +
                                         "(" + x + ", " + y + "): " +
                                         String.format("%#010x", c));
        }
        for (int y = -10; y <= w1.height + 10; y++) {
            int x = w1.width - 20;
            w1.drawPoint(x, y);
            w1.getPointColor(x, y);
            int c = w1.getPointColor(x, y);
            if (c != (y < 0 || y >= w1.height ? 0 : 0x00ff0000))
                throw new AssertionError("Error with getPointColor(): " +
                                         "(" + x + ", " + y + "): " +
                                         String.format("%#010x", c));
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

        w1.setColor("black");
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
