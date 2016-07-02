package gharib.leonar.summoner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Leonar on 6/18/2015.
 */
public class NPC implements Comparable<NPC> {

    protected Map map;
    protected int x;
    protected int y;
    protected int z;
    protected Texture texture;
    protected int direction;
    protected int ID;
    private int mapID;
    private Sprite sprite;
    private String name;
    private String currentChat = "";

    private ArrayList<String> chat;

    protected final static int TILE_LENGTH = 64;

    public NPC(int ID, int x, int y, int MAPID, String texturePath, String name )
    {
        this.ID = ID;
        this.x = x;
        this.y = y;
        mapID = MAPID;
        this.z = 0;

        String path = "characters/" + texturePath;
        this.texture = new Texture(new FileHandle(new File(path)));

        this.name = name;

        sprite = new Sprite(texture);

        sprite.setX(x);
        sprite.setY(y);
        direction = 0;

        chat = new ArrayList<String>();
    }
    public NPC(int ID)
    {
        this.ID = ID;
    }
    /* What happens when the player attempts to talk to this NPC
     * @return - the String returned when the player speaks to the NPC
     */
    public String talk()
    {
        return "This is a test!";
    }
    public Sprite getSprite() {
        sprite.setX(x*TILE_LENGTH);
        sprite.setY(y*TILE_LENGTH);
        setZ(y);
        return sprite;
    }
    public String getName(){
        return name;
    }
    public int getMapID() { return mapID;}
    public int compareTo(NPC npc){
        if ( z < npc.z ) return -1;
        else if ( z > npc.z ) return 1;
        return 0;
    }
    public void setZ(int i){
        z = i;
    }

    public void addChat(String s){
        chat.add(s);
        currentChat = chat.get(0);
    }
    public String getChat(){
        return currentChat;
    }
    public void print(){
        System.out.println("Name: " + name + "\nID: " + ID + "\nmapID: " + mapID);
    }

}
