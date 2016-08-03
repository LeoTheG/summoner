package gharib.leonar.summoner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/* Author       : Leonar Gharib
   Date Created : 7/20/2016
   Purpose      : Monster used in battling.
*/
public class Spirit {

   private int permStrength;
   private int permMana;
   private int permHealth;
   private int permDefense;
   private int permSpeed;
   private int permMaxEnergy;

   private int strength;
   private int mana;
   private int health;
   private int defense;
   private int energy;
   private int maxEnergy;

   private int atkcost;

   private int speed;

   private String name;

   private Sprite sprite;

   private boolean flipped;

   private Spell spell;

   public Spirit ( int str, int m, int hp, int def, int spd, int maxenergy ){

      permStrength = str;
      permMana = m;
      permHealth = hp;
      permDefense = def;
      permMaxEnergy = maxenergy;
      permSpeed = speed;

      strength = str;
      mana = m;
      health = hp;
      defense = def;
      maxEnergy = maxenergy;
      speed = spd;

      energy = 0;

      sprite = new Sprite(new Texture(Gdx.files.internal("data/monsters/blue1.png")));

      flipped = false;

      atkcost = 10;

      spell = new Spell("Bolt", 15, 10, 20);
   }
   public Spell getSpell(){
      return spell;
   }
   public boolean getFlipped() { return flipped; }
   public void flip(){
      flipped = true;
      sprite.flip(true,false);
   }

   public int getAtkCost(){
      return atkcost;
   }

   public int getEnergy(){
      return energy;
   }

   public int getMaxEnergy(){
      return permMaxEnergy;
   }

   public Sprite getSprite(){
      return sprite;
   }

   public boolean cost(int spellIndex){
      int c = 0;
      // cost for normal attack
      if(spellIndex == 0){
         c = 10;
      }

      if ( energy < c ) return false;

      energy -= c;
      return true;
   }
   public void setName(String n){
      name = n;
   }
   public void print(){
      System.err.println(name + " :\nHP : " + health + "\nSpeed : " + speed);
   }

   public int getHP(){
      return health;
   }
   public int getMaxHP() { return permHealth; }

   public int getAttackDamage(){
      return strength;
   }

   public void attackedBy(Spirit s){

      // normal attack is spell id 0
      if ( s.cost(0) ) {

         int diff = defense - s.strength;
         if (diff < 0) health += diff;

         if ( health < 0 ) health = 0;
      }

   }

   public void battleReset(){
      health = permHealth;
      strength = permStrength;
      defense = permDefense;
      maxEnergy = permMaxEnergy;

      energy = 0;
   }

   public void regenEnergy(){
      energy += speed;
      if ( energy > maxEnergy ) energy = maxEnergy;
   }



}
