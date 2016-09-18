package gharib.leonar.summoner;

import java.util.ArrayList;

/* Author       : Leonar Gharib
   Date Created : 8/29/2016
   Purpose      : Used to hold Player's items
*/
public class Bag {

   private Player player;
   private ArrayList<Item> items;

   public enum type {
      POTION, BERRY
   }
   public enum identifier {
      RED
   }

   public Bag(Player p){
      player = p;
      items = new ArrayList<Item>();
   }
   public void addItem(type t, identifier id){
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
   }
   public void useItem(int index, Spirit s){
      if ( index >= 0 && index < items.size() ) {
         Item i = items.remove(index);

         i.use(s);
      }
   }
   public int getNumItems(){
      return items.size();
   }
   public ArrayList<Item> getItems(){
      return items;
   }

}

abstract class Item {

   public abstract boolean use(Spirit s);
   public abstract String getName();
   public abstract void setName(String s);

}

class Potion extends Item {

   private String name;

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
}

class Berry extends Item {

   private String name;

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
}