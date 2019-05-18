import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SeamCarver {

    private Picture picture;
    // private Picture initialPicture;

    private double[][] energyCache;

    // for the seam finding
    private double[] distTo;
    private int[] edgeTo;
    private int seam[];

    private boolean transposed;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {

        if (picture == null) throw new java.lang.IllegalArgumentException();

        // this.initialPicture = new Picture(picture);
        this.picture = new Picture(picture);

        this.energyCache = new double[picture.width()][picture.height()];
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                energyCache[i][j] = -1;
            }
        }

        this.transposed = false;
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {

        if (transposed) {
            return picture.height();
        }

        return picture.width();
    }

    // height of current picture
    public int height() {
        if (transposed) {
            return picture.width();
        }

        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= picture.width())  throw new java.lang.IllegalArgumentException();
        if (y < 0 || y >= picture.height()) throw new java.lang.IllegalArgumentException();

        if (energyCache[x][y] != -1) return energyCache[x][y];
        updateEnergy(x, y);
        return energyCache[x][y];
    }

    private void updateEnergy(int x, int y) {
        int borderPixelEnergy = 1000;

        if (x == 0 || (x + 1) == picture.width()) {
            energyCache[x][y] = borderPixelEnergy;
            return;
        }

        if (y == 0 || (y + 1) == picture.height()) {
            energyCache[x][y] = borderPixelEnergy;
            return;
        }

        int dxSquared = calcSqrtDelta(
                picture.getRGB(x + 1, y),
                picture.getRGB(x - 1, y)
        );

        int dySquared = calcSqrtDelta(
                picture.getRGB(x, y - 1),
                picture.getRGB(x, y + 1)
        );

        energyCache[x][y] = java.lang.Math.sqrt(dxSquared + dySquared);
    }


    private int calcSqrtDelta(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >>  8) & 0xFF;
        int b1 = (rgb1 >>  0) & 0xFF;

        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >>  8) & 0xFF;
        int b2 = (rgb2 >>  0) & 0xFF;

        int dr = r1 - r2;
        int dg = g1 - g2;
        int db = b1 - b2;

        return dr*dr + dg*dg + db*db;
    }

    private void transpose() {
        transposed = !transposed;

        Picture oldPicture = new Picture(picture);

        picture = new Picture(oldPicture.height(), oldPicture.width());

        double[][] oldEnergyCache = new double[oldPicture.width()][oldPicture.height()];

        for (int i = 0; i < oldPicture.width(); i++) {
            for (int j = 0; j < oldPicture.height(); j++) {
                picture.setRGB(j, i, oldPicture.getRGB(i,j));
                oldEnergyCache[i][j] = energyCache[i][j];
            }
        }

        energyCache = new double[picture.width()][picture.height()];
        for (int i = 0; i < oldPicture.width(); i++) {
            for (int j = 0; j < oldPicture.height(); j++) {
                energyCache[j][i] = oldEnergyCache[i][j];
            }
        }
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!transposed) {
            transpose();
        }

        return findVerticalSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        // Edge case for 1 pixel high
        if (height() == 1) {
            return new int[] {0};
        }

        AcyclicSP();
        return seam;
    }

    private void AcyclicSP() {

        // Topological order os going from top to bottom (y = 0 to y = height)
        // For simplicity's sake, lets from from left to right (x = 0 to x = width)


        // Each vertex at (i, j) is connected to vertices: (i-1, j+1), (i, j+1) and (i+1, j+1)
        // For this implementation, cosider that the edge weight
        // is the energy of the destination node.

        // Add two virtual nodes: one at the top and one at the bottom, connected
        // to all the the top and bottom pixels, respectively

        int numberNodes = picture.width()*picture.height() + 2;

        distTo = new double[numberNodes];
        edgeTo = new int[numberNodes];


        // Algs, page 646
        distTo[0] = 0;
        for (int i = 1; i < numberNodes; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }

        for (int z = 0; z < numberNodes; z++) {
            Point point = new Point(this, picture.width(), picture.height(), z);
            for (int e : point.adj()) {
                relax(point, new Point(this, picture.width(), picture.height(), e));
            }
        }

        int[] path = new int[picture.height()];
        path[picture.height() - 1] = edgeTo[numberNodes - 1];
        for (int i = picture.height() - 2; i > 0; i--) {
            path[i] = edgeTo[path[i+1]];
        }

        seam = new int[picture.height()];

        seam[0] = new Point(this, picture.width(), picture.height(), path[1]).x;
        for (int i = 1; i < picture.height() - 1; i++) {
            seam[i] = new Point(this, picture.width(), picture.height(), path[i]).x;
        }
        seam[picture.height() - 1] = seam[picture.height() - 2];

    }


    private void relax(Point pointFrom, Point pointTo) {
        if (distTo[pointTo.z] > distTo[pointFrom.z] + pointTo.energy()) {
            distTo[pointTo.z] = distTo[pointFrom.z] + pointTo.energy();
            edgeTo[pointTo.z] = pointFrom.z;
        }
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length == 0) throw new java.lang.IllegalArgumentException();

        if (!transposed) {
            transpose();
        }

        removeVerticalSeam(seam);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam)      {
        if (seam == null || seam.length == 0) throw new java.lang.IllegalArgumentException();

        // Validate seam
        if (seam.length != picture.height()) throw new java.lang.IllegalArgumentException("seam has invalid length");
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1) throw new java.lang.IllegalArgumentException();
        }


        if (transposed) {
            transpose();
        }


        Picture oldPicture = new Picture(picture);

        picture = new Picture(picture.width() - 1, picture.height());

        int j = -1;

        for (int s: seam) {
            j++;

            for (int i = 0;     i < s;       i++)
                picture.setRGB(i, j, oldPicture.getRGB(i, j));

            for (int i = s; i < picture.width(); i++)
                picture.setRGB(i, j, oldPicture.getRGB(i + 1, j));
        }

    }


    public static void main(String[] args) {

        Picture picture = new Picture(args[0]);

        SeamCarver sc = new SeamCarver(picture);

        StdOut.printf("image is %d columns by %d rows\n", sc.width(), sc.height());
        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.0f ", sc.energy(col, row));
            StdOut.println();
        }




        sc.removeHorizontalSeam(new int[] {1, 1, 1, 1, 1, 1});
        //sc.removeVerticalSeam(new int[] {1, 1, 1, 1, 1});
        //StdOut.printf("image is %d columns by %d rows\n", sc.width(), sc.height());
        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.0f ", sc.energy(col, row));
            StdOut.println();
        }



        /*
        StdOut.printf("width() = %d\n", sc.width());
        StdOut.printf("height() = %d\n", sc.height());
        for (int z = 0; z < 32; z++) {
            Point a = new Point(sc, z);
            StdOut.printf("%d: %s ~> %s\n", z, a.toString(), Arrays.toString(a.adj()));
            //StdOut.printf("%d: %s\n", z, Arrays.toString(a.adj()));
//             a.printPoint();
            //sc.Point(z).printPoint();
        }

         */

        //sc.AcyclicSP();




        StdOut.printf("%s\n", Arrays.toString(sc.findVerticalSeam()));


    }

}