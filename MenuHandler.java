package gharib.leonar.summoner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

/* Author       : Leonar Gharib
   Date Created : 6/29/2016
   Purpose      :
*/
public class MenuHandler {

   public enum states {FREE, INCHAT}
   private states s;
   private ArrayList<Sprite> sprites;

   private static int TILE_LENGTH = 64;
   private static final int WIDTH = 1280;
   private static final int HEIGHT = 1280;

   private String currentChat = "";

   private Sprite textBox = new Sprite (new Texture("UI/gray-box.png"));

   private Player player;

   public MenuHandler(){
      s = states.FREE;
      sprites = new ArrayList<Sprite>();
      //textBox.scale(3.5f);
      textBox.setScale(6.5f,3.5f);
   }
   public void setPlayer(Player p ){player = p;}
   public void pressedSpace(){
      // check to see what current state of menu is
      if ( s == states.FREE ){
         if ( currentChat.length() == 0 ) return;
         System.out.println("Adding textBox");
         sprites.add(textBox);
         s = states.INCHAT;
      }
      else if ( s == states.INCHAT ){
         System.out.println("Removing textBox");
         sprites.remove(textBox);
         s = states.FREE;
      }
      // draw chat box

   }
   public states getState(){
      return s;
   }
   // used for drawing sprites in SummonerGame
   public Sprite[] getSprites(){
      Sprite[] arr = new Sprite[sprites.size()];
      Sprite[] arrs = sprites.toArray(arr);

      for ( int i = 0; i < arrs.length; i++ ){
         if ( arrs[i] == textBox) {
            textBox.setX(player.getX() - textBox.getWidth()/2);
            textBox.setY(player.getY() - HEIGHT/4 + 60 );
         }
      }

      return sprites.toArray(arr);
   }
   public String getChat(){
      return currentChat;
   }
   public void setChat(String str){
      currentChat = str;
   }
}
