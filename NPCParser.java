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
      }
      catch(Exception e){}

      int pos = 0;

      while ( i < 10 ){
         try {
            line = in.readLine();

            // parse first line -- # total NPCS
            if (i == 0) {
               numTotalNPCs = Integer.parseInt(line);
            }
            // parse second line -- blank
            else if (i == 1) ;
               // parse third line -- ID
            else if (i == 2) {
               pos = line.indexOf(ID) + ID.length();
               NPCID = Integer.parseInt(line.substring(pos));
            } else if (i == 3) {
               pos = line.indexOf(NAME) + NAME.length();
               NPCName = line.substring(pos);
            } else if (i == 4) {
               pos = line.indexOf(X) + X.length();
               NPCX = Integer.parseInt(line.substring(pos));
            } else if (i == 5) {
               pos = line.indexOf(Y) + Y.length();
               NPCY = Integer.parseInt(line.substring(pos));
            } else if (i == 6) {
               pos = line.indexOf(TEXTURE) + TEXTURE.length();
               NPCTexture = line.substring(pos);
            } else if (i == 7) {
               pos = line.indexOf(MAPID) + MAPID.length();
               NPCMapID = Integer.parseInt(line.substring(pos));
            } else if (i == 8) ;
            else if (i == 9) {
               NPCText = line;
            }
         } catch (Exception e2){
            System.err.println("ERROR : " + e2.getMessage());
         }
         i++;

      }

      NPC npc = new NPC(NPCID,NPCX,NPCY,NPCMapID,NPCTexture,NPCName);

      npcs.add(npc);

      npc.addChat(NPCText);

      // ensure that NPC can be added to proper map slot
      while (npcList.size() <= NPCMapID ) npcList.add(new ArrayList<Integer>());

      // add NPCID to NPCMapID slot in npcList(main indices of array represent map IDs)
      npcList.get(NPCMapID).add(NPCID);

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
         npcs.get(mapList.get(i)).print();
         arr[i] = npcs.get(mapList.get(i));
         i++;
      }

      return arr;
   }
}