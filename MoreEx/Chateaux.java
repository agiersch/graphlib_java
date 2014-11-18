import java.util.*;

class Chateaux {

    static final Scanner input = new Scanner(System.in);
    static final Random random = new Random();

    /* Note : les coordonnées réelles vont de -100 à +100 en abscisse, et
     *  de -10 à +140 en ordonnée
     */
    static final double rXMin = -100.0;
    static final double rXMax = 100.0;
    static final double rYMin = -10.0;
    static final double rYMax = 140.0;
    static final double hauteurMin = 10;
    static final double hauteurMax = 130;
    static final double largeurMin = 40;
    static final double largeurMax = 150;
    static final double ventMax = 30;
    static final double largeurChateau = 8.5;
    static final double hauteurChateau = 7;
    static final double positionChateau1 = -85.0;
    static final double positionChateau2 = 85.0;
    static final double g = 9.81;
    static final double k = 0.005;
    static final double dt = 0.05;
    static int nbJoueurs = 2;
    static int score1 = 0;
    static int score2 = 0;
    static double largeurMont;
    static double hauteurMont;
    static double wnd;

    /* Retourne un nombre pseudo-aléatoire compris entre le paramètre
     * 'min' (inclus) et le paramètre 'max' (exclus)
     */
    static double frand(double min, double max)
    {
        return min + (max - min)* (random.nextDouble());
    }

    // conversion coordonnées réelles -> coordonnées fenêtre
    static int rtowX(DrawingWindow w, double rx)
    {
        return (int)Math.round((w.width - 1) * (rx - rXMin) / (rXMax - rXMin));
    }

    static int rtowY(DrawingWindow w, double ry)
    {
        return (int )Math.round((w.height - 1) * (rYMax - ry) / (rYMax - rYMin));
    }

    // conversion coordonnées fenêtre -> coordonnées réelles
    static double wtorX(DrawingWindow w, int wx)
    {
        return (rXMax - rXMin) * wx / (w.width - 1) + rXMin;
    }

    static double wtorY(DrawingWindow w, int wy)
    {
        return -(rYMax - rYMin) * wy / (w.height - 1) + rYMax;
    }

    static double hauteurMontagne(double largeur, double hauteur, double x)
    {
        double rx = 2.0 * x / largeur;
        return hauteur * (1.0 - rx * rx);
    }

    static void dessineTerrain(DrawingWindow w, double largeur, double hauteur)
    {
        int y0 = rtowY(w, 0) + 1;
        int xmin = rtowX(w, -largeur / 2.0) - 1;
        int xmax = rtowX(w, largeur / 2.0) + 1;
        w.setColor("forestgreen");
        for (int x = xmin; x <= xmax; x++) {
            double rx = wtorX(w, x);
            double ry = hauteurMontagne(largeur, hauteur, rx);
            int y = rtowY(w, ry);
            if (y <= y0)
                w.drawLine(x, y0, x, y);
        }
        w.setColor("maroon");
        w.fillRect(0, y0 + 1, w.width - 1, w.height - 1);
    }

    static void dessineChateau(DrawingWindow w, double position)
    {
        w.setColor("black");
        w.setColor("darkslategray");
        int y1 = rtowY(w, 0) + 1;
        int h0 = rtowY(w, 3);
        int h1 = rtowY(w, 4);
        for (int i = 0; i < 7; i++) {
            int h = i % 2 != 0 ? h0 : h1;
            int x1 = rtowX(w, position + i - 3.5);
            int x2 = rtowX(w, position + i - 2.5) - 1;
            w.fillRect(x1, y1, x2, h);
        }
        w.setColor("dimgray");
        h0 = rtowY(w, 6);
        h1 = rtowY(w, 7);
        for (int i = 0; i < 5; i++) {
            int h = i % 2 != 0 ? h0 : h1;
            int x1 = rtowX(w, position + i - 8.5);
            int x2 = rtowX(w, position + i - 7.5) - 1;
            w.fillRect(x1, y1, x2, h);
            x1 = rtowX(w, position + i + 3.5);
            x2 = rtowX(w, position + i + 4.5) - 1;
            w.fillRect(x1, y1, x2, h);
        }
    }

    static void dessineVent(DrawingWindow w, double vitesse)
    {
        int lg = rtowX(w, vitesse) - rtowX(w, 0);
        int dir = lg > 0 ? 1 : -1;
        int y = 20;
        w.setColor("black");
        if (lg == 0) {
            w.drawCircle(w.width / 2, y, 4);
        } else {
            int x1 = (w.width - lg) / 2;
            int x2 = (w.width + lg) / 2;
            w.drawLine(x1 - dir, y - 1, x2 - dir, y - 1);
            w.drawLine(x1, y, x2, y);
            w.drawLine(x1 - dir, y + 1, x2 - dir, y + 1);
            for (int i = 0; i < 3; i++) {
                w.drawLine(x2 - i * dir, y, x2 - (6 + i) * dir, y - 4);
                w.drawLine(x2 - i * dir, y, x2 - (6 + i) * dir, y + 4);
            }
        }
    }

    static void dessineExplosion(DrawingWindow w, double rx, double ry)
    {
        final int maxray = rtowX(w, 2.5) - rtowX(w, 0);
        // 1/2 rouge -> rouge -> jaune
        final int x = rtowX(w, rx);
        final int y = rtowY(w, ry);
        int i;
        for (i = 0; i <= maxray / 3; i++) {
            w.setColor(0.5f + 3.0f * i / (2.0f * maxray), 0.0f, 0.0f);
            w.drawCircle(x, y, i);
            DrawingWindow.msleep(20);
        }
        for (/* i */; i < maxray; i++) {
            w.setColor(1.0f, 1.5f * i / maxray - 0.5f, 0.0f);
            w.drawCircle(x, y, i);
            DrawingWindow.msleep(20);
        }
        w.setColor("skyblue");
        for (i = 0; i < maxray; i++) {
            w.drawCircle(x, y, i);
            DrawingWindow.msleep(10);
        }
        //    w.fillCircle(x, y, maxray - 1);
    }

    static void dessineFlammes(DrawingWindow w, double x0, double y0)
    {
        for (int i = 0; i < 70; i++) {
            double dt = 0.05;
            double vx = frand(-2.5, 2.5);
            double vy = frand(5, 17);
            double x = x0;
            double y = y0;
            float red = (float)frand(0.5, 1);
            float green = (float)frand(0, red);
            float blue = 0;
            w.setColor(red, green, blue);
            while (y >= 0.0) {
                w.drawPoint(rtowX(w, x), rtowY(w, y));
                x += vx * dt;
                y += vy * dt;
                vy -= 9.81 * dt;
            }
            DrawingWindow.msleep(30);
        }
    }

    static void initialise(DrawingWindow w)
    {
        largeurMont = frand(largeurMin, largeurMax);
        hauteurMont = frand(hauteurMin, hauteurMax);
        wnd = frand(-ventMax, ventMax);
        w.setBgColor("skyblue");
        w.clearGraph();
        dessineTerrain(w, largeurMont, hauteurMont);
        dessineVent(w, wnd);
        dessineChateau(w, positionChateau1);
        dessineChateau(w, positionChateau2);
        w.setColor("wheat");
        w.drawText(rtowX(w, positionChateau1 - largeurChateau), rtowY(w, 0) + 20,
                   "Joueur 1");
        w.drawText(rtowX(w, positionChateau2 - largeurChateau), rtowY(w, 0) + 20,
                   "Joueur 2");
        w.drawText(rtowX(w, 0) - 15, rtowY(w, 0) + 20, score1 + " / " + score2);
    }

    /* Retour : numéro du perdant, 0 sinon
       x et y contiennent les coordonnées de la collision
    */
    static class ResTir {
        int perdant;
        double x;
        double y;
    }
    static ResTir tir(DrawingWindow w,
                      double x0, double y0, double v0, double alpha) {
        double vx = v0 * Math.cos(alpha);
        double vy = v0 * Math.sin(alpha);
        double x = x0;
        double y = y0;
        int collision = 0;
        do {
            int wx = rtowX(w, x);
            int wy = rtowY(w, y);
            w.setColor("black");
            w.fillCircle(wx, wy, 2);
            double vxr = vx - wnd;
            double kvr = -k * Math.sqrt(vxr * vxr + vy * vy);
            double ax = kvr * vxr;
            double ay = kvr * vy - g;
            x += vx * dt;
            y += vy * dt;
            vx += ax * dt;
            vy += ay * dt;

            DrawingWindow.msleep(10);
//             w.sync();
            w.setColor("skyblue");
            w.fillCircle(wx, wy, 2);
//             w.setColor("black");
//             w.drawPoint(wx, wy);
            if (y <= 0) {
                collision = 3;
            } else if (y < hauteurChateau) {
                if (positionChateau1 - largeurChateau <= x
                    && positionChateau1 + largeurChateau >= x)
                    collision = 1;
                else if (positionChateau2 - largeurChateau <= x
                         && positionChateau2 + largeurChateau >= x)
                    collision = 2;
            }
            if (collision == 0) {
                double h = hauteurMontagne(largeurMont, hauteurMont, x);
                if (h > 0 && y < h)
                    collision = 3;
            }
        } while (collision == 0);
        ResTir res = new ResTir();
        res.x = x;
        res.y = y;
        res.perdant = collision == 3 ? 0 : collision;
        return res;
    }

    static int jeu1(DrawingWindow w)
    {
        initialise(w);
        int joueur = 2;
        double x, y;
        int perdant;
        do {
            joueur = 3 - joueur;
            w.sync();
            System.out.println("-=| Joueur " + joueur + " |=-");
            double alpha;
            double v0;
            if (joueur <= nbJoueurs) {
                System.out.print("\nangle ? ");
                alpha = input.nextDouble();
                System.out.print("vitesse initiale ? ");
                v0 = input.nextDouble();
            } else {
                alpha = frand(10, 90);
                v0 = frand(10, 100);
                System.out.println(" [ " + (int )alpha + "° ; " + (int )v0 + " ]");
            }
            alpha = Math.toRadians(alpha);
            double x0;
            if (joueur == 1) {
                x0 = positionChateau1 + 0.8 * largeurChateau;
            } else {
                x0 = positionChateau2 - 0.8 * largeurChateau;
                alpha = Math.PI - alpha;
            }
            double y0 = hauteurChateau + 1;
            {
                ResTir r = tir(w, x0, y0, v0, alpha);
                x = r.x;
                y = r.y;
                perdant = r.perdant;
            }
            dessineExplosion(w, x, y);
            dessineVent(w, wnd);
        } while (perdant == 0);
        dessineFlammes(w, x, y);
        String msg = " Joueur " + perdant;
        if (perdant == joueur)
            msg += " s'est suicidé ! ";
        else
            msg += " a perdu ! ";
        w.setColor("darkred");
        w.setBgColor("white");
        w.drawText(w.width / 2, w.height / 3, msg);
        w.sync();
        System.out.println(msg);
        return perdant;
    }

    public static void main(String[] args) {
        DrawingWindow w = new DrawingWindow("Chateaux", 640, 480);

        if (args.length > 0)
            nbJoueurs = Integer.parseInt(args[0]);

        boolean rejouer = true;
        do {
            int perdant = jeu1(w);
            if (perdant == 1)
                score2++;
            else if (perdant == 2)
                score1++;
            System.out.println("### SCORE : " + score1 + " / " + score2 + " ###");
            if (nbJoueurs == 0)
                DrawingWindow.sleep(2);
            else {
                char r;
                do {
                    System.out.println("Recommencer (o/n) ? ");
                    r = input.next().charAt(0);
                } while (r != 'o' && r != 'n');
                rejouer = r == 'o';
            }
        } while (rejouer);
        w.closeGraph();
    }
}
