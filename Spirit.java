package gharib.leonar.summoner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/* Author       : Leonar Gharib
   Date Created : 7/20/2016
   Purpose      : Monster used in battling.
*/
public class Spirit {

   private static int ATK_COST = 10;

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
   private int manaRegen;

   private int atkcost;

   private int speed;

   private String name;

   private Sprite sprite;

   private boolean flipped;
   private boolean flopped;

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
      flopped = false;

      manaRegen = 5;
   }

   public boolean getFlipped() { return flipped; }
   public boolean getFlopped() { return flopped; }

   public void flip(){
      flipped = true;
      sprite.flip(true,false);
   }

   public void unFlop(boolean enemy){
      if ( enemy )
         sprite.rotate(270);
      else
         sprite.rotate(90);
   }

   public void flop(boolean enemy){
      if ( enemy ) {
         sprite.rotate(90);
      }
      else
         sprite.rotate(270);

      flopped = true;
   }

   public int getDefense(){ return defense; }
   public int getMana(){ return mana; }
   public int getAtkCost(){
      return ATK_COST;
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
   public void setHP(int hp){
      this.health = hp;
   }
   public int getMaxMana(){ return permMana; }

   public int getAttackDamage(){
      return strength;
   }

   public void loseMana(int m){
      mana -= m;
   }
   public void loseEnergy(int e){
      energy -= e;
   }

   public void takeDamage(int amt){
      health -= amt;
   }

   public void attackedBy(Spirit s){

      // normal attack is spell id 0
      if ( energy > ATK_COST ) {

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

   public void gainHP(int amt){
      health += amt;
      if ( health > permHealth ) health = permHealth;
   }
   public void gainMP(int amt){
      mana += amt;
      if ( mana > permMana ) mana = permMana;
   }
   public void gainEnergy(int amt){
      energy += amt;
      if ( energy > permMaxEnergy ) energy = permMaxEnergy;
   }

   public void regenEnergy(){
      energy += speed;
      if ( energy > maxEnergy ) energy = maxEnergy;
   }
   public void regenMana(){
      mana += manaRegen;
      if ( mana > permMana ) mana = permMana;
   }



}
