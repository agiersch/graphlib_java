import java.util.*;

class JeuDeLaVie {

    static final Random random = new Random();

    static final int LARGEUR = 1200;
    static final int HAUTEUR = 900;

    static final int VIVANT = 0x000000ff; // bleu
    static final int MORT   = 0x00ffffff; // blanc

    static class Cell {
        int neigh;
        boolean now;
        boolean next;
    };
    static Cell[][] cells;

    static void majVoisins(int i, int j, int delta)
    {
        for (int k = i - 1 ; k <= i + 1 ; ++k)
            for (int l = j - 1 ; l <= j + 1 ; ++l)
                if (k != i || l != j) {
                    int vi = (k + LARGEUR) % LARGEUR;
                    int vj = (l + HAUTEUR) % HAUTEUR;
                    cells[vi][vj].neigh += delta;
                }
    }

    static void dessine(DrawingWindow w, int i, int j, int couleur)
    {
        w.setColor(couleur);
        w.drawPoint(i, j);
    }

    static void init(DrawingWindow w)
    {
        cells = new Cell[LARGEUR][HAUTEUR];
        for (int i = 0 ; i < LARGEUR ; ++i)
            for (int j = 0 ; j < HAUTEUR ; ++j)
                cells[i][j] = new Cell();
        for (int i = 0 ; i < LARGEUR ; ++i)
            for (int j = 0 ; j < HAUTEUR ; ++j)
                cells[i][j].neigh = 0;
        for (int i = 0 ; i < LARGEUR ; ++i)
            for (int j = 0 ; j < HAUTEUR ; ++j) {
                cells[i][j].now = cells[i][j].next = random.nextBoolean();
                if (cells[i][j].now) {
                    majVoisins(i, j, 1);
                    dessine(w, i, j, VIVANT);
                }
            }
    }

    static void update0(DrawingWindow w)
    {
        for (int i = 0 ; i < LARGEUR ; ++i)
            for (int j = 0 ; j < HAUTEUR ; ++j) {
                switch (cells[i][j].neigh) {
                case 2:
                    // cells[i][j].next = cells[i][j].now; // useless
                    break;
                case 3:
                    cells[i][j].next = true;
                    break;
                default:
                    cells[i][j].next = false;
                    break;
                }
            }
    }
    static void update1(DrawingWindow w)
    {
        for (int i = 0 ; i < LARGEUR ; ++i)
            for (int j = 0 ; j < HAUTEUR ; ++j) {
                if (cells[i][j].now) {
                    if (!cells[i][j].next) {
                        cells[i][j].now = false;
                        majVoisins(i, j, -1);
                        dessine(w, i, j, MORT);
                    }
                } else {
                    if (cells[i][j].next) {
                        cells[i][j].now = true;
                        majVoisins(i, j, 1);
                        dessine(w, i, j, VIVANT);
                    }
                }
            }
    }

    public static void main(String[] args) {
        DrawingWindow w = new DrawingWindow("Jeau de la vie", LARGEUR, HAUTEUR);
        w.setBgColor(MORT);
        w.clearGraph();
        init(w);
        w.sync();
        for (int gen = 0 ; ; ++gen) {
            if (gen % 10 == 0)
                System.err.println("generation " + gen);
            update0(w);
            update1(w);
            w.sync();
        }
    }
}
