package gharib.leonar.summoner;

/* Author       : Leonar Gharib
   Date Created : 7/20/2016
   Purpose      : Used in battling.
*/
public class Spell {

   private int damage;
   private int energycost;
   private int magiccost;
   private String name;
   private int spellID;

   private boolean onCooldown;
   private int ticksPassed;

   public Spell(String n, int dmg, int mgc, int engc, int spellID ){
      name = n;
      damage = dmg;
      magiccost = mgc;
      energycost = engc;
      this.spellID = spellID;

      onCooldown = false;
   }
   public int getSpellID() { return spellID; }
   public int getMagicCost() { return magiccost; }
   public String getName(){
      return name;
   }
   public int getDamage(){
      return damage;
   }
   public int getEnergyCost(){
      return energycost;
   }
   public boolean isOnCooldown(){
      return onCooldown;
   }



}
