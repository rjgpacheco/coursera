/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Point {

    // used to make conversitons between 2d poit and and a 1d matrix
    public final int x, y, z;
    private int width, height;
    private SeamCarver sc;

    public Point(SeamCarver sc, int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.z = (x + width*y) + 1;
        this.sc = sc;
    }

    public Point(SeamCarver sc, int width, int height, int z) {
        this.width = width;
        this.height = height;
        z = z - 1;
        this.y = z / width;
        this.x = z - this.y*width;
        this.z = z+1;
        this.sc = sc;
    }


    private boolean isVirtual() {
        if (this.z == 0)
            return true;

        if (this.z == (width * height + 1))
            return true;

        return false;
    }

    private int xyToZ(int x, int y) {
        return (x + this.width*y) + 1;
    }

    public double energy() {
        if (isVirtual())
            return 0;
        return sc.energy(this.x, this.y);
    }


    public int[] adj() {

        if (this.z == width * height + 1) {
            return new int[] {};
        }

        if (this.z == 0) {
            int[] adj = new int[width];
            for (int i = 0; i < width; i++)
                adj[i] = i+1;
            return adj;
        }

        if (this.y == (height - 1)) {
            return new int[] {width * height+ 1};
        }

        if (this.x == 0) {
            return new int[] {
                    xyToZ(x + 0, y + 1),
                    xyToZ(x + 1, y + 1)
            };
        }

        if (this.x == (width - 1)) {
            return new int[] {
                    xyToZ(x - 1, y + 1),
                    xyToZ(x + 0, y + 1)
            };
        }

        return new int[] {
                xyToZ(x - 1, y + 1),
                xyToZ(x + 0, y + 1),
                xyToZ(x + 1, y + 1)
        };

    }

    public String toString() {
        return String.format("%2d: (%2d, %2d)", this.z, this.x, this.y);
    }


    public static void main(String[] args) {

    }
}
