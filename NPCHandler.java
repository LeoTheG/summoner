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

   private PriorityQueue<NPC> pq = new PriorityQueue<NPC>();

   private SpriteBatch batch;

   public NPCHandler(SpriteBatch batch){
      this.batch = batch;

   }
   public void clearNPCs(){
      pq.clear();
   }
   public void add(NPC[] npc){

      for ( int i = 0; i < npc.length; i++){
         pq.add(npc[i]);
      }
   }

   public void add(NPC npc){
      pq.add(npc);
   }

   public void render(){
      NPC[] arr1 = new NPC[pq.size()];
      NPC[] arr = pq.toArray(arr1);

      //System.err.println("Size of arr = " + arr.length);

      for ( int i = 0; i < arr.length; i++ ){
         arr[i].getSprite().draw(batch);
      }
   }

   public String getTopName(){
      NPC npc = pq.peek();
      return npc.getName();
   }
   public void popTop(){
      pq.poll();
   }
}