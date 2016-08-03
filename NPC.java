package gharib.leonar.summoner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.File;
import java.util.ArrayList;

/* Author       : Leonar Gharib
   Date Created : 6/18/2015
   Purpose      : Implement NPC class which holds properties about various NPCs in the world.
                  NPCs are created through NPCParser.
*/
public class NPC implements Comparable<NPC> {

    protected Map map;
    protected int x;
    protected int y;
    protected Texture texture;
    protected int direction;
    protected int ID;
    private int mapID;
    private Sprite sprite;
    private String name;
    private String currentChat = "";
    private boolean battle;

    private int chatLevel = 0;

    private ArrayList<String> chat;

    protected final static int TILE_LENGTH = 64;

    // maximum amount of characters allowed on a line
    private static final int MAX_NUM_CHAR = 16;
    private static final int MAX_NUM_TEXT = 68;

    /** Constructor
     *
     * @param ID - unique NPC ID
     * @param x - x position
     * @param y - y position
     * @param MAPID - map on which NPC appears
     * @param texturePath - path to texture image
     * @param name - name of NPC
     */
    public NPC(int ID, int x, int y, int MAPID, String texturePath, String name) {
        this.ID = ID;
        this.x = x;
        this.y = y;
        this.mapID = MAPID;

        String path = "data/characters/" + texturePath;
        this.texture = new Texture(Gdx.files.internal(path));

        this.name = name;

        sprite = new Sprite(texture);

        sprite.setX(x);
        sprite.setY(y);
        direction = 0;

        chat = new ArrayList<String>();

        battle = false;
    }

    /** Determine if NPC can battle or not
     *
     * @return - true if NPC can battle, false otherwise.
     */
    public boolean getBattle() {
        if (ID == 1) return true;
        else return false;
    }

    /** Sets x,y values for sprite and returns sprite
     *
     * @return - sprite with correct x,y values
     */
    public Sprite getSprite() {
        sprite.setX(x * TILE_LENGTH);
        sprite.setY(y * TILE_LENGTH);
        return sprite;
    }

    /** Getter method for name
     *
     * @return - name of NPC
     */
    public String getName() {
        return name;
    }

    /** Getter method for mapID
     *
     * @return - mapID
     */
    public int getMapID() {
        return mapID;
    }

    // PQ ordered by top being highest y value.
    // needed for 

    /** Compare function for priority queue. Orders top priority as highest y value.
     *  Used to organize sprite draw order by highest to lowest y value.
     *
     * @param npc - npc to compare to
     * @return - 1 if NPCs y is less than other NPCs y value. -1 if other NPC has less
     *           y value than original NPC. 0 if equal y values.
     */
    public int compareTo(NPC npc) {
        if (y > npc.y) return -1;
        else if (y < npc.y) return 1;
        return 0;
    }

    public void resetChatLevel(){
        chatLevel = 0;
    }

    public boolean finishedChat(){
        if ( chatLevel >= chat.size() ){

            resetChatLevel();

            return true;
        }
        return false;
    }

    /** Add chat options to NPC */
    public void addChat(String s) {

        int i = MAX_NUM_TEXT;

        while ( i < s.length() ){

            int count = 0;

            // find first index that is space before point
            while ( s.charAt(i) != ' ' ){
                count++;
                i--;
            }
            String portion = s.substring(i - MAX_NUM_TEXT + count, i);

            chat.add(portion);

            i += MAX_NUM_TEXT + 1;

        }

        i -= MAX_NUM_TEXT;

        chat.add( s.substring( i, s.length() ) );

        for ( int j = 0; j < chat.size(); j++ ) {
            // fix the text block and reinsert into arraylist
            chat.set(j, fixChat(chat.get(j)) );
        }

    }

    private String fixChat(String str){

        int i = MAX_NUM_CHAR;
        int len = str.length();

        while ( i <= len - 1 ){

            // replace space with a newline
            if ( str.charAt(i) != ' ' ){

                // i holds index of first space previous and closest to original index or 0
                while ( i >= 0 ){
                    if ( str.charAt(i) == ' ' ) break;
                    i--;
                }

            }
            // replace space with newline

            str = str.substring(0,i) + "\n" + str.substring(i+1);

            i += MAX_NUM_CHAR;
        }

        return str;

    }

    /** Return current chat option prompt */
    public String getChat(boolean incLevel) {

        if ( chatLevel >= chat.size() ) {
            if(incLevel)
                chatLevel++;

            String lastChat = chat.get(chat.size()-1);

            return lastChat;
        }

        currentChat = chat.get(chatLevel);

        if ( chatLevel <= chat.size() )
            if(incLevel)
                chatLevel++;

        return currentChat;
    }

    /** Print information about NPC (used in debugging) */
    public void print() {
        System.out.println("Name: " + name + "\nID: " + ID + "\nmapID: " + mapID);
    }

}
