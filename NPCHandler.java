package gharib.leonar.summoner;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/* Author       : Leonar Gharib
   Date Created : 6/26/2016
   Purpose      :
*/
public class NPCHandler {

   private static int TILE_LENGTH = 64;

   private PriorityQueue<NPC> pq = new PriorityQueue<NPC>();

   private SpriteBatch batch;

   public NPCHandler(SpriteBatch batch){
      this.batch = batch;

   }
   public void clearNPCs(){
      pq.removeAll(pq);
   }
   public void add(NPC[] npc){

      for ( int i = 0; i < npc.length; i++){
         pq.add(npc[i]);
      }
   }

   public void add(NPC npc){
      pq.add(npc);
   }

   public void render(Player p){

      // want to render highest Y value first to lowest Y value last

      NPC[] arr1 = new NPC[pq.size()];
      NPC[] arr = pq.toArray(arr1);
      int playerY = p.getY() / TILE_LENGTH;

      int counter = 0;

      // i is first index where player Y has higher Y value than element (NPC)
      int i;
      for (  i = 0; i < arr.length; i++ ) {
         if ( playerY > arr[i].y ){
            System.err.println("playerY " + playerY + " is greater than y value " + arr[i].y);
            break;
         }
      }

      int j;
      for ( j = 0; j <= i - 1; j++) {
         arr[j].getSprite().draw(batch);
         System.err.println("Render (" + counter+") : " + arr[j].y);
         counter++;
      }

      p.getPlayerSprite().draw(batch);
      System.err.println("Render (" + counter+") : " + playerY);
      counter++;

      for(int k = j; k < arr.length; k++ ){
         arr[k].getSprite().draw(batch);
         System.err.println("Render (" + counter+") : " + arr[k].y);
         counter++;
      }

      /*

      System.err.println("PQ ORDER TOP TO BOTTOM: " );
      for ( int j = 0; j < arr.length; j++ ) System.err.println(arr[j].y);



      int higherYIndex;

      // find first index where player's Y is higher than or equal to element's Y
      for ( higherYIndex = 0; higherYIndex < arr.length; higherYIndex++ ) {
         if ( playerY < arr[higherYIndex].y ) {
            System.err.println("Player Y: " + playerY + " is less than " + arr[higherYIndex].y);
            break;
         }
      }

      // higherYIndex = index of first NPC that has a higher Y value than player's Y value
      // therefore have to render all of the lower index NPCs first, then player, then rest of
      // NPCs in order to render properly based off of lower Y value = priority rendering

      //System.err.println("Size of arr = " + arr.length);

      int counter = 0;

      if ( higherYIndex >= arr.length ) higherYIndex = arr.length - 1;

      for ( int i = 0; i <= higherYIndex; i++ ){
         System.err.println("Rendering : " + counter + " : " + arr[i].y);
         counter++;
         arr[i].getSprite().draw(batch);
      }
      System.err.println("Rendering : " + counter + " : " + playerY);
      counter++;
      p.getPlayerSprite().draw(batch);
      for ( int i = higherYIndex + 1; i < arr.length; i++ ) {
         System.err.println("Rendering : " + counter + " : " + arr[i].y);
         counter++;
         arr[i].getSprite().draw(batch);
      }
      */

   }

   public String getTopName(){
      NPC npc = pq.peek();
      return npc.getName();
   }
   public void popTop(){
      pq.poll();
   }
}