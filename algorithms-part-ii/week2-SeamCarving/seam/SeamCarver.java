import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {

    private Picture picture;

    private double[][] energyCache;

    // for the seam finding
    private double[] distTo;
    private int[] edgeTo;
    // private int[] seam;

    private boolean isVertical;

    private CoordinateTransformer ct;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new java.lang.IllegalArgumentException();
        this.picture = new Picture(picture);
        resetEnergy();
        this.ct = new CoordinateTransformer();
        this.isVertical = true;

    }

    private class CoordinateTransformer {

        public CoordinateTransformer() {
            // Nothing to do, actually
        }

        private int width() {
            return picture.width();
            // return width;
        }

        private int height() {
            return picture.height();
            // return height;
        }


        public int convert(int x, int y) {
            return (x + width()*y) + 1;
        }

        public int getY(int z) {
            return (z - 1) / width();
        }

        public int getX(int z) {
            return (z - 1) - getY(z)*width();
        }

        public int transposed(int z) {
            return convert(getY(z), getX(z));
        }

        public Iterable<Integer> adj2(int z) {
            if (isVertical)
                return adjDown(z);

            // if (!isVertical)
            return adjRight(z);
        }


        public Iterable<Integer> adjDown(int z) {
            int x = getX(z);
            int y = getY(z);

            Stack<Integer> stack = new Stack<>();

            // Point is the virtual source, return the first row of pixels
            if (z == virualSource()) {
                for (int i = 0; i < width(); i++)
                    stack.push(i+1);
                return stack;
            }

            // Point is the virtual sink, return enpty
            if (z == virtualSink()) {
                return stack;
            }

            // Point is in the last row of pixles, return the virtual sink
            if (y == (picture.height() - 1)) {
                stack.push(virtualSink());
                return stack;
            }

            stack.push(convert(x, y + 1)); // Point below

            if (x > 0)
                stack.push(convert(x - 1, y + 1));

            if (x < (width() - 1))
                stack.push(convert(x + 1, y + 1));

            return stack;
        }


        public Iterable<Integer> adjRight(int z) {
            int x = getX(z);
            int y = getY(z);

            Stack<Integer> stack = new Stack<>();

            // Point is the virtual source, return the first row of pixels
            if (z == virualSource()) {
                for (int i = 0; i < picture.height(); i++)
                    stack.push(convert(0, i));
                    // stack.push(i+1);
                return stack;
            }

            // Point is the virtual sink, return enpty
            if (z == virtualSink()) {
                return stack;
            }

            // Point is in the last row of pixles, return the virtual sink
            if (x == (picture.width() - 1)) {
                stack.push(virtualSink());
                return stack;
            }

            stack.push(convert(x + 1, y)); // Point below

            if (y > 0)
                stack.push(convert(x + 1, y - 1));

            if (y < (picture.height() - 1))
                stack.push(convert(x + 1, y + 1));

            return stack;
        }

    }

    private int virualSource() {
        return 0;
    }

    private int virtualSink() {
        return picture.width() * picture.height() + 1;
    }

    private void resetEnergy() {
        this.energyCache = new double[picture.width()][picture.height()];
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                energyCache[i][j] = -1;
            }
        }
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        // if (x < 0 || x >= picture.width())  throw new java.lang.IllegalArgumentException();
        // if (y < 0 || y >= picture.height()) throw new java.lang.IllegalArgumentException();

        if (x < 0 || x >= width())  throw new java.lang.IllegalArgumentException();
        if (y < 0 || y >= height()) throw new java.lang.IllegalArgumentException();

        if (energyCache[x][y] != -1) return energyCache[x][y];
        updateEnergy(x, y);
        return energyCache[x][y];
    }


    private int[] topological() {

        int[] topological = new int[picture.height() * picture.width() + 2];

        topological[0] = 0;
        topological[topological.length - 1] = picture.height() * picture.width() + 1;

        int k = 0;

        // For upright images
        if (isVertical) {
            for (int y = 0; y < picture.height(); y++) {
                for (int x = 0; x < picture.width(); x++) {
                    k++;
                    topological[k] = ct.convert(x, y);
                }
            }
        }

        // For transposed images
        if (!isVertical) {
            for (int x = 0; x < picture.width(); x++) {
                for (int y = 0; y < picture.height(); y++) {
                    k++;
                    topological[k] = ct.convert(x, y);
                }
            }
        }

        return topological;
    }

    private double energy(int z) {
        // StdOut.println(z);

        // StdOut.printf("z = %d; picture.width() * picture.height() + 1 = %d\n", z, picture.width() * picture.width() + 1);
        // If yes, then this is a "virtual" point
        if (z == (picture.width() * picture.height() + 1))
            return 0;

        return energy(ct.getX(z), ct.getY(z));
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

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findHorizontalSeam2();

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findVerticalSeam2();
    }

    private int[] getPath() {
        Stack<Integer> thisSeam = new Stack<>();

        int i = edgeTo.length - 1;
        while (true) {

            if (i == 0)
                break;

            thisSeam.push(edgeTo[i]);
            i = edgeTo[i];

        }

        int[] seam2 = new int[thisSeam.size()-1];
        int k = -1;
        thisSeam.pop();
        for (int ii: thisSeam) {
            k++;
            seam2[k] = ii;
        }

        return seam2;
    }

    private void AcyclicSP2() {

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

        for (int z : topological()) {
            for (int e: ct.adj2(z)) {
                // StdOut.printf("%2d ~> %s\n",z,  Arrays.toString(ct.adj(z)));
                relax(z, e);
            }
        }

        // return edgeTo;
    }

    private int[] getSeam() {
        int[] path = getPath();
        int[] thisSeam = new int[path.length];

        if (isVertical) {
            for (int i = 0; i < thisSeam.length; i++) {
                thisSeam[i] = ct.getX(path[i]);
            }
        }

        if (!isVertical) {
            for (int i = 0; i < thisSeam.length; i++) {
                thisSeam[i] = ct.getY(path[i]);
            }
        }

        return thisSeam;
    }


    private int[] findVerticalSeam2() {
        isVertical = true;
        AcyclicSP2();
        return getSeam();
    }

    private int[] findHorizontalSeam2() {
        isVertical = false;
        AcyclicSP2();
        return getSeam();
    }

    private void relax(int zFrom, int zTo) {
        if (distTo[zTo] > distTo[zFrom] + energy(zTo)) {
            distTo[zTo] = distTo[zFrom] + energy(zTo);
            edgeTo[zTo] = zFrom;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length == 0) throw new java.lang.IllegalArgumentException();

        // Validate seam
        if (seam.length != picture.width()) throw new java.lang.IllegalArgumentException("seam has invalid length");
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1) throw new java.lang.IllegalArgumentException();
        }

        Picture oldPicture = new Picture(picture);

        picture = new Picture(picture.width(), picture.height() - 1);

        int i = -1;

        for (int s: seam) {
            i++;

            for (int j = 0;     j < s;       j++)
                picture.setRGB(i, j, oldPicture.getRGB(i, j));

            for (int j = s; j < picture.height(); j++)
                picture.setRGB(i, j, oldPicture.getRGB(i, j + 1));
        }

        oldPicture = null;
        resetEnergy();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam)      {
        if (seam == null || seam.length == 0) throw new java.lang.IllegalArgumentException();

        // Validate seam
        if (seam.length != picture.height()) throw new java.lang.IllegalArgumentException("seam has invalid length");
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1) throw new java.lang.IllegalArgumentException();
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


        oldPicture = null;
        resetEnergy();

    }


    public static void main(String[] args) {

        /*
        Picture picture = new Picture(args[0]);

        SeamCarver sc = new SeamCarver(picture);

        StdOut.printf("image is %d columns by %d rows\n", sc.width(), sc.height());
        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.0f ", sc.energy(col, row));
            StdOut.println();
        }


        // Test adjacent
        for (int z = 0; z < 32; z++) {
            // StdOut.printf("%2d ~> %s\n",z,  Arrays.toString(sc.ct.adj(z)));
        }

         */

        /*
        // Test cooordinate transform
        StdOut.printf("Test sc.ct\n");
        StdOut.printf("sc.transposed = %10b, ", sc.transposed);
        StdOut.printf("sc.ct.width() = %d, ", sc.ct.width());
        StdOut.printf("sc.ct.height() = %d \n", sc.ct.height());

        sc.transpose();

        StdOut.printf("sc.transposed = %10b, ", sc.transposed);
        StdOut.printf("sc.ct.width() = %d, ", sc.ct.width());
        StdOut.printf("sc.ct.height() = %d \n", sc.ct.height());

        StdOut.printf("\n\n\n");

         */

        // findHorizontalSeam
         // int[] verticalSeam = sc.findHorizontalSeam();
         // StdOut.printf("findHorizontalSeam: %s\n", Arrays.toString(verticalSeam));


        // Check if energy() is sensible to transposition
        /*
        int x, y;
        x = StdRandom.uniform(sc.width());
        y = StdRandom.uniform(sc.height());

        for (int i = 0; i < 50; i++) {
            x = StdRandom.uniform(sc.width());
            y = StdRandom.uniform(sc.height());

            StdOut.printf("(%d, %d)", x, y);
            StdOut.printf("%9.0f", sc.energy(x, y));
            sc.transpose();
            StdOut.printf("%9.0f", sc.energy(x, y));
            sc.transpose();
            StdOut.printf("%9.0f", sc.energy(x, y));
            StdOut.println();

        }

         */


        // Test topological order
        Picture picture = new Picture(5, 2);

        //( picture.set(0, 0, null);

        Integer[] a =  {0,0,1};

        Integer[][] c = new Integer[3][3];

        StdOut.println(c[0][0] == null);

        /*
        Picture picture = new Picture(args[0]);
        SeamCarver carver = new SeamCarver(picture);

        // Topological order and connected
        StdOut.printf("Topological order and connected for vertical\n");
        carver.isVertical = true;
        for (int e: carver.topological()) {
            StdOut.printf("%2d ~> (%2d, %2d)", e, carver.ct.getX(e), carver.ct.getY(e));
            StdOut.printf(" ::");
            for (int v: carver.ct.adj2(e))
                StdOut.printf(" %2d", v);

            StdOut.println();
        }


        StdOut.printf("Topological order and connected for horizontal\n");
        carver.isVertical = false;
        for (int e: carver.topological()) {
            StdOut.printf("%2d ~> (%2d, %2d)", e, carver.ct.getX(e), carver.ct.getY(e));
            StdOut.printf(" ::");
            for (int v: carver.ct.adj2(e))
                StdOut.printf(" %2d", v);

            StdOut.println();
        }

         */



        /*
        carver.isVertical = false;
        carver.AcyclicSP2();
        int[] pathTo = carver.edgeTo;
        StdOut.println();
        StdOut.printf("%s\n", Arrays.toString(pathTo));


        Stack<Integer> seam = new Stack<>();

        int i = pathTo.length - 1;
        while (true) {

            if (i == 0)
                break;

            seam.push(pathTo[i]);
            i = pathTo[i];

            StdOut.printf(":: %d\n", i);

        }

        int[] seam2 = new int[seam.size()-1];
        int k = -1;
        seam.pop();
        for (int ii: seam) {
            k++;
            seam2[k] = ii;
        }

        StdOut.println();
        for (int j : seam2) {
            StdOut.printf(" %2d", j);
        }
        StdOut.println();

         */




        // Remove verrtical seams unntil only 1 pixel wide

    }



}