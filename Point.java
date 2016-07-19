package gharib.leonar.summoner;

/* Author       : Leonar Gharib
   Date Created : 6/25/2016
   Purpose      : Conveniently hold x and y values.
*/
public class Point {

    public int x;
    public int y;

    /** Holds an x,y value (coordinate) */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Comparison of two Points
     *
     * @param p - point to compare
     * @return - true if both x and y values of both points are equal. False otherwise.
     */
    public boolean equals(Point p) {
        return (p.x == x && p.y == y);
    }
}
