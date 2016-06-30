package gharib.leonar.summoner;

/* Author       : Leonar Gharib
   Date Created : 6/25/2016
   Purpose      :
*/
public class Point {

   public int x;
   public int y;

   public Point(int x, int y){
      this.x = x;
      this.y = y;
   }
   public boolean equals(Point p){
      return ( p.x == x && p.y == y );
   }
}
