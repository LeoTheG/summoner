package gharib.leonar.summoner;

/* Author       : Leonar Gharib
   Date Created : 7/20/2016
   Purpose      : Used in battling.
*/
public class Spell {

   private int damage;
   private int speedcost;
   private int magiccost;
   private String name;

   private boolean onCooldown;
   private int ticksPassed;

   public Spell(String n, int dmg, int mgc, int spdc ){
      name = n;
      damage = dmg;
      magiccost = mgc;
      speedcost = spdc;

      onCooldown = false;
   }
   public String getName(){
      return name;
   }
   public int getDamage(){
      return damage;
   }
   public int getSpeedCost(){
      return speedcost;
   }
   public boolean isOnCooldown(){
      return onCooldown;
   }



}
