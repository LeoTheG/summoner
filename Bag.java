package gharib.leonar.summoner;

import java.util.ArrayList;

/* Author       : Leonar Gharib
   Date Created : 8/29/2016
   Purpose      : Used to hold Player's items
*/
public class Bag {

   private Player player;
   //private ArrayList<Item> items;
   int[] items;

   public enum type {
      POTION, BERRY
   }
   public enum identifier {
      RED
   }
   private static int TOTAL_NUM_ITEMS = 1;

   public Bag(Player p){
      player = p;
      items = new int[TOTAL_NUM_ITEMS];

      //items = new ArrayList<Item>();
   }
   /*
   public void addItem(type t, identifier id){

      System.err.println("Adding item to bag of size " + items.length );

      if ( t == type.POTION ) {

         if ( id == identifier.RED ) {
            items.add( new Potion ( identifier.RED ) );
         }

      }

      else if ( t == type.BERRY ) {

         if ( id == identifier.RED ) {
            items.add( new Berry(identifier.RED));
            System.err.println("Acquired Red Berry");
         }


      }
      System.err.println("Added item to bag now of size " + items.length );
   }
   */
   public int getNumTotalItems(){
      return TOTAL_NUM_ITEMS;
   }
   public void addItem(int ID){

      System.err.println("Incrementing count of item with ID " + ID + " to " + items[ID] + 1);
      items[ID]++;

   }
   /*
   public void useItem(int index, Spirit s){
      if ( index >= 0 && index < items.size() ) {
         Item i = items.remove(index);

         i.use(s);
      }
   }
   */
   public Item getItem(int ID){
      switch(ID){
         case 0:
            return new Berry(identifier.RED);
      }
      return null;
   }
   public void useItem(int index, Spirit s){
      if ( index >= 0 && index < items.length ) {
         Item i = getItem(items[index]);

         i.use(s);
      }
   }
   public int getNumItems(){
      return items.length;
   }
   /*
   public ArrayList<Item> getItems(){
      return items;
   }
   */
   public int[] getItems(){
      return items;
   }

   public String getItemName(int ID){
      switch (ID){
         case 0:
            return "Red Berry";
      }
      return "";
   }

}

abstract class Item {

   protected String name;
   protected int count = 0;

   public abstract boolean use(Spirit s);
   public abstract String getName();
   public abstract void setName(String s);
   public abstract int getCount();
   public abstract void incCount();
   public abstract void decCount();

}

class Potion extends Item {

   int hp_restore;
   int mp_restore;
   int eng_restore;

   public Potion(Bag.identifier id){

      hp_restore = 0;
      mp_restore = 0;
      eng_restore = 0;

      if ( id == Bag.identifier.RED ) {
         hp_restore = 10;
         name = "Red ";
      }

      name += "Potion";
   }
   public boolean use(Spirit s){
      s.gainHP(hp_restore);
      s.gainMP(mp_restore);
      s.gainEnergy(eng_restore);

      return true;
   }
   public String getName(){
      return name;
   }
   public void setName(String str){
      name = str;
   }
   public int getCount(){
      return count;
   }
   public void incCount(){
      count++;
   }
   public void decCount(){
      count--;
   }
}

class Berry extends Item {

   public Berry(Bag.identifier id){

      if ( id == Bag.identifier.RED ) {
         name = "Red ";
      }

      name += "Berry";
   }

   public boolean use (Spirit s){
      return false;
   }
   public String getName(){
      return name;
   }
   public void setName(String str){
      name = str;
   }
   public int getCount(){
      return count;
   }
   public void incCount(){
      count++;
   }
   public void decCount(){
      count--;
   }
}