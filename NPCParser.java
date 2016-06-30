package gharib.leonar.summoner;

import java.io.BufferedReader;
import java.io.FileReader;

/* Author       : Leonar Gharib
   Date Created : 6/24/2016
   Purpose      :
*/
public class NPCParser {

   public int numNPCs;
   public NPC[] npcs;

   public NPCParser(){
      numNPCs = 1;
      npcs = new NPC[numNPCs];
   }

   public void parse(){
      String line = null;
      BufferedReader in = null;

      int ID = 0;
      String name = null;
      int x = 0;
      int y = 0;
      String texturePath = null;
      int MAPID = 0;
      String chat = "";

      try {
         in = new BufferedReader(new FileReader("characters\\NPCS\\NPCS.txt"));
         line = in.readLine();
         int posID = line.indexOf("ID") + "ID".length();
         ID = Integer.parseInt(line.substring(posID), 10);
         System.err.println("ID = " + ID);
      }
      catch(Exception e){System.err.println(e.getMessage());}

      int i = 1;

      //Stores values of NPCs file into variables
      while(i < 7 )
      {
         try {
            line = in.readLine();
            if ( i == 1 ) {
               int posNAME = line.indexOf("NAME") + "NAME".length();
               name = line.substring(posNAME);
               System.err.println("Name = " + name);
            }
            else if ( i == 2 ){
               int posX = line.indexOf("X") + "X".length();
               x = Integer.parseInt(line.substring(posX), 10);
               System.err.println("X = " + x);
            }
            else if ( i == 3 ) {
               int posY = line.indexOf("Y") + "Y".length();
               y = Integer.parseInt(line.substring(posY), 10);
               System.err.println("y = " + y);
            }
            else if ( i == 4 ) {
               int posTexture = line.indexOf("TEXTURE") + "TEXTURE".length();
               texturePath = line.substring(posTexture);
               System.err.println("texturePath = " + texturePath);
            }
            else if ( i == 5 ) {
               int posMAPID = line.indexOf("MAPID") + "MAPID".length();
               MAPID = Integer.parseInt(line.substring(posMAPID));
               System.err.println("MapID = " + MAPID);
            }
            else if ( i == 6 ) {
               chat = line;
               System.err.println("chat: " + chat);
            }

         }
         catch(Exception e2){}
         i++;
      }
      NPC npc = new NPC(ID,x,y,MAPID,texturePath,name);
      npc.addChat(chat);

      npcs[0] = npc;
   }
}