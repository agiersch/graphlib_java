import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.lang.reflect.*;

/**
 * Fenêtre de dessin
 *
 * <p>Cette classe permet d'écrire des applications graphiques simples
 * en dessinant dans une fenêtre.
 *
 * <p><b>NB.</b> Pour toutes les méthodes de dessin, le coin en haut à
 * gauche de la fenêtre a les coordonnées (0, 0).  Le coin en bas à
 * droite de la fenêtre a les coordonnées (largeur - 1, hauteur - 1),
 * si la fenêtre est de dimension largeur × hauteur.
 *
 * <p>Un appui sur la touche &lt;Esc&gt; provoque la fermeture de la
 * fenêtre.  Comme pour la plupart des applications, il est également
 * possible de fermer la fenêtre via le gestionnaire de fenêtres.
 *
 * <p>Télécharger le code: <a href="DrawingWindow.java">DrawingWindow.java</a>
 *
 * <p>Télécharger des exemples d'utilisation:
 * <a href="Hello.java">Hello.java</a>
 * <a href="Exemple1.java">Exemple1.java</a>
 * <a href="Exemple2.java">Exemple2.java</a>
 * <a href="Exemple3.java">Exemple3.java</a>
 *
 * @author Arnaud Giersch &lt;arnaud.giersch@univ-fcomte.fr&gt;
 * @version 20141008
 */
public class DrawingWindow {

    /** Largeur de la fenêtre */
    public final int width;

    /** Hauteur de la fenêtre */
    public final int height;

    /**
     * Construit une nouvelle fenêtre de dessin avec le titre et les dimensions
     * passés en paramètres.
     *
     * @param title             titre de la fenêtre
     * @param width             largeur de la fenêtre
     * @param height            hauteur de la fenêtre
     *
     * @see javax.swing.JPanel
     */
    public DrawingWindow(String title, int width, int height) {

        this.title = new String(title);        
        this.width = width;
        this.height = height;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();

        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() { createGUI(); }
                });
        }
        catch (Exception e) {
            System.err.println("Error: interrupted while creating GUI");
            System.err.println("Got exception: " + e);
            System.exit(1);
        }

        setColor(Color.BLACK);
        setBgColor(Color.WHITE);
        clearGraph();
        sync();
    }

    /**
     * Change la couleur de dessin.
     *
     * @param color         couleur
     *
     * @see java.awt.Color
     * @see #setColor(String)
     * @see #setColor(float, float, float)
     * @see #setBgColor(Color)
     */
    public void setColor(Color color) {
        graphics.setColor(color);
    }

    /**
     * Change la couleur de dessin.
     *
     * Le nom de couleur est de la forme "black", "white", "red", "blue", ...
     *
     * @param name          nom de couleur
     *
     * @see #setColor(Color)
     * @see #setColor(float, float, float)
     * @see #setBgColor(String)
     */
    public void setColor(String name) {
        try {
            Field field = Class.forName("java.awt.Color").getField(name);
            graphics.setColor((Color)field.get(null));
        } catch (Exception e) {
            System.err.println("Warning: color not found: " + name);
        }
    }

    /**
     * Change la couleur de dessin.
     *
     * Les composantes de rouge, vert et bleu de la couleur doivent être
     * compris entre 0 et 1.  Si le trois composantes sont à 0, on obtient
     * du noir; si les trois composantes sont à 1, on obtient du blanc.
     *
     * @param red           composante de rouge
     * @param green         composante de vert
     * @param blue          composante de bleu
     *
     * @see #setColor(Color)
     * @see #setColor(String)
     * @see #setBgColor(float, float, float)
     */
    public void setColor(float red, float green, float blue) {
        setColor(new Color(red, green, blue));
    }

    /**
     * Change la couleur de fond.
     *
     * @param color         couleur
     *
     * @see #setBgColor(String)
     * @see #setBgColor(float, float, float)
     * @see #setColor(Color)
     * @see #clearGraph()
     */
    public void setBgColor(Color color) {
        bgColor = color;
    }

    /**
     * Change la couleur de fond.
     *
     * @param name          nom de couleur
     *
     * @see #setBgColor(Color)
     * @see #setBgColor(float, float, float)
     * @see #setColor(String)
     * @see #clearGraph()
     */
    public void setBgColor(String name) {
        try {
            Field field = Class.forName("java.awt.Color").getField(name);
            bgColor = (Color)field.get(null);
        } catch (Exception e) {
            System.err.println("Warning: color not found: " + name);
        }
    }

    /** Change la couleur de fond.
     *
     * @param red           composante de rouge
     * @param green         composante de vert
     * @param blue          composante de bleu
     *
     * @see #setBgColor(Color)
     * @see #setBgColor(String)
     * @see #setColor(float, float, float)
     * @see #clearGraph()
     */
    public void setBgColor(float red, float green, float blue) {
        bgColor = new Color(red, green, blue);
    }

    /**
     * Efface la fenêtre.
     *
     * La fenêtre est effacée avec la couleur de fond courante.
     *
     * @see #setBgColor
     */
    public void clearGraph() {
        synchronized (image) {
            Color c = graphics.getColor();
            graphics.setColor(bgColor);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(c);
        }
        panel.repaint();
    }

    /** Dessine un point.
     *
     * Dessine un point (pixel) aux coordonnées (x, y), avec la couleur de
     * dessin courante.
     *
     * @see #setColor
     */
    public void drawPoint(int x, int y) {
        synchronized (image) {
            image.setRGB(x, y, graphics.getColor().getRGB());
        }
        panel.repaint(x, y, 1, 1);
    }

    /**
     * Dessine un segment.
     *
     * Dessine un segement de droite entre les coordonnées (x1, y1) et
     * (x2, y2), avec la couleur de dessin courante.
     *
     * @see #setColor
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        synchronized (image) {
            graphics.drawLine(x1, y1, x2, y2);
        }
        panel.repaint(Math.min(x1, x2), Math.min(y1, y2),
                      Math.abs(x1 - x2) + 1, Math.abs(y1 - y2) + 1);
    }

    /** Dessine un rectangle.
     *
     * Dessine le rectangle parallèle aux axes et défini par les
     * coordonnées de deux sommets opposés (x1, y1) et (x2, y2).  Utilise
     * la couleur de dessin courante.
     *
     * @see #fillRect
     * @see #setColor
     */
    public void drawRect(int x1, int y1, int x2, int y2) {
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int w = Math.abs(x1 - x2);
        int h = Math.abs(y1 - y2);
        synchronized (image) {
            graphics.drawRect(x, y, w, h);
        }
        panel.repaint(x, y, w + 1, h + 1);
    }

    /** Dessine un rectangle plein.
     *
     * Dessine le rectangle plein parallèle aux axes et défini par les
     * coordonnées de deux sommets opposés (x1, y1) et (x2, y2).  Utilise
     * la couleur de dessin courante.
     *
     * @see #drawRect
     * @see #setColor
     */
    public void fillRect(int x1, int y1, int x2, int y2) {
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int w = Math.abs(x1 - x2) + 1;
        int h = Math.abs(y1 - y2) + 1;
        synchronized (image) {
            graphics.fillRect(x, y, w, h);
        }
        panel.repaint(x, y, w, h);
    }

    /**
     * Dessine un cercle.
     *
     * Dessine un cercle de centre (x, y) et de rayon r.  Utilise la
     * couleur de dessin courante.
     *
     * @see #fillCircle
     * @see #setColor
     */
    public void drawCircle(int x, int y, int r) {
        synchronized (image) {
            graphics.drawOval(x - r, y - r, 2 * r, 2 * r);
        }
        panel.repaint(x - r, y - r, 2 * r + 1, 2 * r + 1);
    }

    /**
     * Dessine un disque.
     *
     * Dessine un disque (cercle plein) de centre (x, y) et de rayon r.
     * Utilise la couleur de dessin courante.
     *
     * @see #drawCircle
     * @see #setColor
     */
    public void fillCircle(int x, int y, int r) {
        synchronized (image) {
            graphics.drawOval(x - r, y - r, 2 * r, 2 * r);
            graphics.fillOval(x - r, y - r, 2 * r, 2 * r);
        }
        panel.repaint(x - r, y - r, 2 * r + 1, 2 * r + 1);
    }

    /**
     * Écrit du texte.
     *
     * Écrit le texte text, aux coordonnées (x, y).
     */
    public void drawText(int x, int y, String text) {
        synchronized (image) {
            graphics.drawString(text, x, y);
        }
        panel.repaint(); // don't know how to calculate tighter bounding box
    }

    /**
     * Retourne la couleur d'un pixel.
     *
     * Retourne la couleur du pixel de coordonnées (x, y).
     *
     * @return              couleur du pixel
     */
    public int getPointColor(int x, int y) {
        return image.getRGB(x, y);
    }

    /**
     * Synchronise le contenu de la fenêtre.
     *
     * Pour des raisons d'efficacités, le résultat des fonctions de dessin
     * n'est pas affiché immédiatement.  L'appel à sync permet de
     * synchroniser le contenu de la fenêtre.  Autrement dit, cela bloque
     * l'exécution du programme jusqu'à ce que le contenu de la fenêtre
     * soit à jour.
     */
    public void sync() {
        // put an empty action on the event queue, and  wait for its completion
        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() { }
                });
        }
        catch (Exception e) {
        }
    }

    /**
     *  Ferme la fenêtre graphique.
     */
    public void closeGraph() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    WindowEvent ev =
                        new WindowEvent(frame,
                                        WindowEvent.WINDOW_CLOSING);
                    Toolkit.getDefaultToolkit()
                        .getSystemEventQueue().postEvent(ev);
                }
            });
    }


    /**
     * Suspend l'exécution pendant un certain temps.
     *
     * @param secs          temps d'attente en seconde
     */
    static void sleep(long secs) {
        try {
            Thread.sleep(secs * 1000);
        }
        catch (Exception e) {
        }
    }

    /**
     * Suspend l'exécution pendant un certain temps.
     *
     * @param msecs          temps d'attente en millisecondes
     */
    static void msleep(long msecs) {
        try {
            Thread.sleep(msecs);
        }
        catch (Exception e) {
        }
    }

    /**
     * Suspend l'exécution pendant un certain temps.
     *
     * @param usecs          temps d'attente en microsecondes
     */
    static void usleep(long usecs) {
        try {
            Thread.sleep(usecs / 1000, (int)(usecs % 1000) * 1000);
        }
        catch (Exception e) {
        }
    }

    /* PRIVATE STUFF FOLLOWS */

    private final String title; // window's title
    private JFrame frame;       // the frame (window)
    private DWPanel panel;      // the panel showing the image
    private BufferedImage image; // the image we draw into
    private Graphics2D graphics; // graphics associated with image
    private Color bgColor;       // background color, for clearGraph()

    // To be run on the Event Dispatching Thread
    void createGUI() {
        panel = new DWPanel(this);

        frame = new JFrame(title);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(panel);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private class DWPanel extends JPanel implements KeyListener {

        private static final long serialVersionUID = 0;

        final DrawingWindow w;

        DWPanel(DrawingWindow w) {
            this.w = w;
            Dimension dimension = new Dimension(w.width, w.height);
            super.setMinimumSize(dimension);
            super.setMaximumSize(dimension);
            super.setPreferredSize(dimension);
        }

        public void paint(Graphics g) {
            synchronized (w.image) {
                g.drawImage(w.image, 0, 0, null);
            }
        }

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                w.closeGraph();
            }
        }

        public void keyReleased(KeyEvent e) { }
        public void keyTyped(KeyEvent e) { }

    }
}
