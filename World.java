package gharib.leonar.summoner;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;

import java.util.ArrayList;
import java.util.List;

/* Author       : Leonar Gharib
   Date Created : 6/16/2015
   Purpose      : World holds a reference to the current map being rendered and handles changes between maps upon
                  user entering a new map due to doors or other circumstances
*/

public class World {

    private Map currentMap;
    private int currentMapIndex;
    public List<Spot> spots = new ArrayList<Spot>();
    private NPCParser npcParser;
    private NPCHandler npcHandler;
    private static final int FORWARD = 0, RIGHT = 1, BACKWARD = 2, LEFT = 3; //player's direction constants

    private Map[] maps = { new Map("maps\\house2.tmx", 0),
                           new Map("maps\\outside1.tmx", 1)
                         };
    private NPC[] npcs;

    public World(NPCParser npcParser)
    {
        this.npcParser = npcParser;
        currentMapIndex = 0;
        currentMap = maps[currentMapIndex]; //begin inside map house1
        npcParser.parse();
        npcs = npcParser.getNPCS(currentMapIndex);

        // add spot(s)
        for ( int i = 0; i < npcs.length; i++) {
            spots.add(new Spot(new Point(npcs[i].x, npcs[i].y), npcs[i]));
        }
        npcHandler = null;
        npcs = npcParser.getNPCS(currentMapIndex);
        if ( npcs == null ) System.err.println("npcs is null -- constructor");
    }
    public void setNPCHandler(NPCHandler n){
        npcHandler = n;
    }
    public NPC[] getNPCs(){
        return npcs;
    }
    public Map getCurrentMap()
    {
        return currentMap;
    }
    public int getCurrentMapIndex() { return currentMapIndex; }
    // changes the map to ID given
    // also returns point which should be updated point of player in new map
    public Point changeMap(int ID)
    {
        currentMap = maps[ID];
        currentMap.updateTiledMapRenderer();
        npcs = npcParser.getNPCS(ID);
        Point p = null;

        switch (ID){
            case 0:
                p = new Point(8,1);
                break;
            case 1:
                p = new Point(3,5);
                break;
        }

        npcHandler.clearNPCs();
        spots.clear();

        npcHandler.add(npcs);

        for ( int i = 0; i < npcs.length; i++){
            spots.add(new Spot(new Point(npcs[i].x, npcs[i].y), npcs[i]));
        }


        return p;
    }
    public TiledMapRenderer getTiledMapRenderer()
    {
        return currentMap.getTiledMapRenderer();
    }

    public boolean occupied(Point p){

        for ( int i = 0; i < spots.size(); i++ ) {
            if ( p.x == spots.get(i).x && p.y == spots.get(i).y){
                return true;
            }
        }
        return false;
    }
    // check NPC at point in map
    public NPC checkSpot(Point p, int direction){
        for ( int i = 0; i < spots.size(); i++){
            if ( spots.get(i).point == p ){
                return spots.get(i).npc;
            }
        }
        return (NPC) null;
    }
    public NPC checkSpot(int x, int y, int direction){
        Point p = null;

        switch(direction){
            case FORWARD:
                p = new Point(x,y+1);
                break;
            case LEFT:
                p = new Point(x-1,y);
                break;
            case RIGHT:
                p = new Point(x+1,y);
                break;
            case BACKWARD:
                p = new Point(x,y-1);
                break;
        }

        //System.err.println("Checking spot " + p.x + ", " + p.y);
        System.out.println("Tile: " + currentMap.getTileName(p.x,p.y));
        for ( int i = 0; i < spots.size(); i++){
            if ( spots.get(i).point.equals(p) ){
                System.out.println("Returning NPC");
                return spots.get(i).npc;
            }
        }
        System.err.println("Returning null");
        return (NPC) null;
    }

}

class Spot{
    public NPC npc;
    public Point point;
    public int x;
    public int y;

    Spot(Point p, NPC npc){
        this.npc = npc;
        this.point = p;
        x = p.x;
        y = p.y;
    }
}