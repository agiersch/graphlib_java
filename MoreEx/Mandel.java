class Mandel {

    static class Parameters {
        // nombre max d'itérations
        int maxiter;
        // zone d'intérêt par défaut
        double Rmin;
        double Rmax;
        double Imin;
        double Imax;
        // facteur d'échelle
        double Rscale;
        double Iscale;
    }

    static final Parameters initial_parameters;
    static {
        initial_parameters = new Parameters();
        initial_parameters.maxiter = 1000;
        initial_parameters.Rmin    = -2.05;
        initial_parameters.Rmax    = 0.55;
        initial_parameters.Imin    = -1.3;
        initial_parameters.Imax    = 1.3;
        initial_parameters.Rscale  = 0.0;
        initial_parameters.Iscale  = 0.0;
    }

    static double sqr(double x)
    {
        return x * x;
    }

    static int check_point(Parameters p, double cr, double ci)
    {
        double zr2, zi2;
        zi2 = sqr(ci);
        if (sqr(cr + 1) + zi2 < 1.0 / 16.0)
            return p.maxiter;
        double x4 = cr - 1.0 / 4.0;
        double q = sqr(x4) + zi2;
        if (q * (q + x4) < zi2 / 4.0)
            return p.maxiter;
        zr2 = sqr(cr);
        double zr = cr;
        double zi = ci;
        int i;
        for (i = 0 ; i < p.maxiter && zr2 + zi2 < 4 ; i++) {
            zi = 2 * zr * zi + ci;
            zr = zr2 - zi2 + cr;
            zr2 = sqr(zr);
            zi2 = sqr(zi);
        }
        return i;
    }

    static void set_color(DrawingWindow w, Parameters p, int i)
    {
        float rouge, vert, bleu;
        if (i >= p.maxiter) {
            rouge = vert = bleu = 0.0f;
        } else {
            int ii = (p.maxiter - 1 - i) % 96;
            if (ii < 32) {
                // vert -> bleu
                bleu = ii / 32.0f;
                vert = 1.0f - bleu;
                rouge = 0.0f;
            } else if (ii < 64) {
                // bleu -> rouge
                rouge = (ii - 32) / 32.0f;
                bleu = 1.0f - rouge;
                vert = 0.0f;
            } else {
                // rouge -> vert
                vert = (ii - 64) / 32.0f;
                rouge = 1.0f - vert;
                bleu = 0.0f;
            }
        }
        w.setColor(rouge, vert, bleu);
    }

    // Fonction de dessin de l'ensemble de Madelbrot, dans la zone
    // spécifiée, et avec la précision souhgaitée.
    static void do_mandel(DrawingWindow w, Parameters p)
    {
        int x, y;                   // le pixel considéré
        double cr, ci;              // le complexe correspondant
        for (y = 0 ; y < w.height ; y++) {
            ci = p.Imax - y * p.Iscale;
            cr = p.Rmin;
            int x0 = 0;
            int i0 = check_point(p, cr, ci);
            for (x = 1 ; x < w.width ; x++) {
                cr = p.Rmin + x * p.Rscale;
                int i = check_point(p, cr, ci);
                if (i != i0) {
                    set_color(w, p, i0);
                    w.drawLine(x0, y, x - 1, y);
                    i0 = i;
                    x0 = x;
                }
            }
            set_color(w, p, i0);
            w.drawLine(x0, y, w.width - 1, y);
        }
    }

    // Fonction de dessin principale, calcule la zone d'intérêt, appelle
    // do_mandel(), pour dessiner l'ensemble, et permet le zoom.
    static void mandel(DrawingWindow w)
    {
        Parameters p = initial_parameters;
        while (true) {
            p.Rscale = (p.Rmax - p.Rmin) / (w.width - 1);
            p.Iscale = (p.Imax - p.Imin) / (w.height - 1);
            do_mandel(w, p);
            w.setColor("white");
            w.drawText(5, 15, "Cliquer sur l'image pour zoomer");
            w.waitMousePress();
            int x = w.getMouseX();
            int y = w.getMouseY();
            // calcul des coordonnées du point cliqué
            double Tr = p.Rmin + x * p.Rscale;
            double Ti = p.Imax - y * p.Rscale;
            // calcul de la nouvelle zone d'intérêt :
            // zoom ×2 en direction du point cliqué
            final int zoom = 2;
            double Rmin2 = p.Rmin / zoom;
            double Rmax2 = p.Rmax / zoom;
            double Imin2 = p.Imin / zoom;
            double Imax2 = p.Imax / zoom;
            double Rshift = Tr - (Rmin2 + Rmax2) / 2;
            double Ishift = Ti - (Imin2 + Imax2) / 2;
            Rmin2 += Rshift;
            Rmax2 += Rshift;
            Imin2 += Ishift;
            Imax2 += Ishift;
            // affichage d'un rectangle autour de la nouvelle zone d'intérêt
            w.setColor("white");
            w.drawRect((int)Math.round((Rmin2 - p.Rmin) / p.Rscale),
                       (int)Math.round((p.Imax - Imin2) / p.Iscale),
                       (int)Math.round((Rmax2 - p.Rmin) / p.Rscale),
                       (int)Math.round((p.Imax - Imax2) / p.Iscale));
            p.Rmin = Rmin2;
            p.Rmax = Rmax2;
            p.Imin = Imin2;
            p.Imax = Imax2;
        }
    }

    public static void main(String[] args) {
        DrawingWindow w = new DrawingWindow("Mandelbrot' set", 800, 800);
        mandel(w);
    }
}
