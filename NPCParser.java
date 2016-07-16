package gharib.leonar.summoner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

/* Author       : Leonar Gharib
   Date Created : 6/24/2016
   Purpose      :
*/
//TODO: Begin NPCS.txt with # representing number of NPCS
//TODO: Begin every section with # of NPCs in that map
//TODO: Better parse NPCS
public class NPCParser {

   private static String ID = "ID";
   private static String NAME = "NAME";
   private static String X = "X";
   private static String Y = "Y";
   private static String TEXTURE = "TEXTURE";
   private static String MAPID = "MAPID";

   private ArrayList<ArrayList<Integer>> npcList = new ArrayList<ArrayList<Integer>>();
   private ArrayList<NPC> npcs = new ArrayList<NPC>();

   public int numNPCs;
   //public NPC[] npcs;

   public NPCParser(){
      numNPCs = 1;
      //npcs = new NPC[numNPCs];
      //npcs = new NPC[numNPCs];
   }

   public void parse(){
      System.err.println("Parsing");
      String line = null;
      BufferedReader in = null;

      int i = 0;

      int numTotalNPCs = 0;
      int NPCID = 0;
      String NPCName = "";
      int NPCX = 0;
      int NPCY = 0;
      String NPCTexture = "";
      int NPCMapID = 0;
      String NPCText = "";

      try{
         in = new BufferedReader(new FileReader("characters\\NPCS\\NPCS.txt"));
            line = in.readLine();

            // parse 1st line -- # total NPCS
            numTotalNPCs = Integer.parseInt(line);
         //System.err.println("Num total NPCS: "+ numTotalNPCs);
      }
      catch(Exception e){}

      int pos = 0;

      while ( i < (9 * numTotalNPCs + 1) ){

        int index = i % 9;

         //System.err.println("~~index = " + index);

         try {
            line = in.readLine();

            //System.err.println("Read line: " + line);

            switch ( index ) {
              case 0:
                 //System.err.println("case 0 : skip");
                break;
              case 1:

                pos = line.indexOf(ID) + ID.length();
                NPCID = Integer.parseInt(line.substring(pos));
                 //System.err.println("ID: " + NPCID);
                break;
              case 2:
                pos = line.indexOf(NAME) + NAME.length();
                NPCName = line.substring(pos);
                 //System.err.println("Name: " + NPCName);
                break;
              case 3:
                pos = line.indexOf(X) + X.length();
                NPCX = Integer.parseInt(line.substring(pos));
                 //System.err.println("X: " + NPCX);
                break;
              case 4:
                pos = line.indexOf(Y) + Y.length();
                NPCY = Integer.parseInt(line.substring(pos));
                 //System.err.println("Y: " + NPCY);
                break;
              case 5:
                pos = line.indexOf(TEXTURE) + TEXTURE.length();
                NPCTexture = line.substring(pos);
                 //System.err.println("Texture: " + NPCTexture);
                break;
              case 6:
                pos = line.indexOf(MAPID) + MAPID.length();
                NPCMapID = Integer.parseInt(line.substring(pos));
                 //System.err.println("MapID: " + NPCMapID);
                break;
              case 7:
                 //System.err.println("Skip");
                break;
              case 8:
                NPCText = line;
                 //System.err.println("Text: " + line);
                break;
            }
            if ( index == 8) {
               //System.err.println("Making new NPC at mapID: " + NPCMapID);

               NPC npc = new NPC(NPCID,NPCX,NPCY,NPCMapID,NPCTexture,NPCName);

               npcs.add(npc);

               npc.addChat(NPCText);

               // ensure that NPC can be added to proper map slot
               while (npcList.size() <= NPCMapID ) npcList.add(new ArrayList<Integer>());

               // add NPCID to NPCMapID slot in npcList(main indices of array represent map IDs)
               npcList.get(NPCMapID).add(NPCID);
            }
        }
        catch (Exception e2){
            System.err.println("ERROR : " + e2.getMessage());
        }
         i++;

      }

   }
   // returns array of NPCs that appear on certain map
   public NPC[] getNPCS(int mapID){

      while (npcList.size() <= mapID ) npcList.add(new ArrayList<Integer>());

      // list of IDs on map with ID "mapID"
      ArrayList<Integer> mapList = npcList.get(mapID);
      System.err.println("There are " + mapList.size() + " npcs in mapID " + mapID);
      int i = 0;
      int endID = mapList.size() - 1;

      System.err.println("getNPCS making array of size " + (endID + 1));

      NPC[] arr = new NPC[endID + 1];

      while ( i <= endID ){
         // grabs NPC from npcs arraylist according toID given by mapList and
         // stores into array
         //npcs.get(mapList.get(i)).print();
         arr[i] = npcs.get(mapList.get(i));
         i++;
      }

      return arr;
   }
}
