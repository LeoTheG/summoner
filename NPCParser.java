package gharib.leonar.summoner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

/* Author       : Leonar Gharib
   Date Created : 6/24/2016
   Purpose      : Parse NPCs from file into organized lists.
*/

public class NPCParser {

    // Properties of NPCs
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String X = "X";
    private static final String Y = "Y";
    private static final String TEXTURE = "TEXTURE";
    private static final String MAPID = "MAPID";

    private static BufferedReader in;

    // number of lines per NPC that describes its properties
    private static final int NUM_PROP_LINE = 9;

    // list of NPCs by mapID
    private ArrayList<ArrayList<Integer>> npcList = new ArrayList<ArrayList<Integer>>();
    // list of all NPCs
    private ArrayList<NPC> npcs = new ArrayList<NPC>();

    /** Constructor which parses file upon creation */
    public NPCParser() {
        try {
            FileReader NPCFile = new FileReader("characters\\NPCS\\NPCS.txt");
            in = new BufferedReader(NPCFile);
        }
        catch(Exception e){System.err.println(e.getMessage());}
        parse();
    }

    /** Parses file which has specific formatting containing information about
     *  all NPCs in the game.
     */
    public void parse() {
        // read line-by-line
        String line;

        int i = 0;

        int numTotalNPCs = 0;
        int NPCID = 0;
        String NPCName = "";
        int NPCX = 0;
        int NPCY = 0;
        String NPCTexture = "";
        int NPCMapID = 0;
        String NPCText = "";

        try {
            line = in.readLine();

            // parse 1st line -- # total NPCS
            numTotalNPCs = Integer.parseInt(line);
            //System.err.println("Num total NPCS: "+ numTotalNPCs);
        }
        catch (Exception e) {System.err.println("ERRORAF: " + e.getMessage()); }

        int pos = 0;

        while (i < (NUM_PROP_LINE * numTotalNPCs + 1)) {

            int index = i % NUM_PROP_LINE;

            //System.err.println("~~index = " + index);

            try {

                line = in.readLine();

                //System.err.println("Read line: " + line);

                switch (index) {
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
                        //NPCText = line;
                        //System.err.println("Text: " + line);
                        break;
                }
                // after last line for NPC, create NPC object and add to lists
                if (index == NUM_PROP_LINE - 1) {

                    /*
                    while ( line.length() != 0 ){
                        NPCText += line;
                        line = in.readLine();
                    }
                    */

                    NPCText = NPCName + ": " + line;

                    NPC npc = new NPC(NPCID, NPCX, NPCY, NPCMapID, NPCTexture, NPCName);
                    npc.addChat(NPCText);

                    npcs.add(npc);

                    // ensure that NPC can be added to proper map slot
                    while (npcList.size() <= NPCMapID) npcList.add(new ArrayList<Integer>());

                    // add NPCID to NPCMapID slot in npcList(main indices of array represent map IDs)
                    npcList.get(NPCMapID).add(NPCID);

                }
            }
            catch (Exception e2) {
                System.err.println("ERROR : " + e2.getMessage());
                e2.printStackTrace();
            }

            i++;

        }

    }

    // returns array of NPCs that appear on certain map
    public NPC[] getNPCS(int mapID) {

        // make certain that npcList has enough lists (one for each mapID)
        while (npcList.size() <= mapID) npcList.add(new ArrayList<Integer>());

        // list of IDs on map with ID "mapID"
        ArrayList<Integer> mapList = npcList.get(mapID);

        int i = 0;
        int endID = mapList.size() - 1;

        NPC[] arr = new NPC[mapList.size()];

        // copies all NPCs from list of certain mapID into an array
        while (i <= endID) {
            arr[i] = npcs.get(mapList.get(i));
            i++;
        }

        return arr;
    }
}
