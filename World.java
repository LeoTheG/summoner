package gharib.leonar.summoner;

import com.badlogic.gdx.Preferences;
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

    private static final int FORWARD = 0, RIGHT = 1, BACKWARD = 2, LEFT = 3; //player's direction constants

    private Map currentMap;
    private int currentMapIndex;

    public List<Spot> spots = new ArrayList<Spot>();

    private NPCParser npcParser;
    private NPCHandler npcHandler;

    private Point point;

    private Map[] maps = {new Map("maps\\house2.tmx", 0),
            new Map("maps\\outside1.tmx", 1),
            new Map("maps\\battleGrass.tmx", 2)
    };
    private NPC[] npcs;

    /**
     * Constructor for World class
     *
     * @param p         - Preferences (save file)
     * @param npcParser - Used to
     */
    public World(Preferences p, NPCParser npcParser) {
        this.npcParser = npcParser;
        // load map index
        currentMapIndex = p.getInteger("mapID", 0);

        if ( currentMapIndex == 2 ) {
            currentMapIndex = 1;
        }

        currentMap = maps[currentMapIndex];

        // load NPCs
        npcs = npcParser.getNPCS(currentMapIndex);

        // point (x,y) at which player spawns when entering a new map (depends on map)
        point = new Point(0, 0);

        // add spot(s) -- tiles occupied by NPCs
        for (int i = 0; i < npcs.length; i++) {
            spots.add(new Spot(new Point(npcs[i].x, npcs[i].y), npcs[i]));
        }

        npcHandler = null;

    }

    /**
     * Used to give access to npcHandler
     *
     * @param n - npcHandler to hand off to World class
     */
    public void setNPCHandler(NPCHandler n) {
        npcHandler = n;
        npcHandler.add(npcs);
    }

    /**
     * Getter method which returns currentMap
     *
     * @return - currentMap
     */
    public Map getCurrentMap() {
        return currentMap;
    }

    /**
     * Getter method which returns currentMapIndex
     *
     * @return - currentMapIndex
     */
    public int getCurrentMapIndex() {
        return currentMapIndex;
    }

    /**
     * Changes mapID to given ID and determines point at which player spawns on map change
     *
     * @param ID - mapID that map is changed to
     * @return - new point at which player spawns upon changing map
     */
    public Point changeMap(int ID) {
        currentMapIndex = ID;

        // change map, update tiles, and get NPCS on new map
        currentMap = maps[ID];
        currentMap.updateTiledMapRenderer();
        npcs = npcParser.getNPCS(ID);

        // clear list of current NPCs in npcHandler and add new NPCs from map change
        npcHandler.clearNPCs();
        npcHandler.add(npcs);

        // remove original spots and create new ones
        spots.removeAll(spots);
        for (int i = 0; i < npcs.length; i++) {
            spots.add(new Spot(new Point(npcs[i].x, npcs[i].y), npcs[i]));
        }

        // determines player's new x and y values depending on map
        switch (ID) {
            case 0:
                point.x = 8;
                point.y = 1;
                break;
            case 1:
                point.x = 3;
                point.y = 5;
                break;
            case 2:
                System.err.println("Changing playerX and playerY to 10,10");
                point.x = 10;
                point.y = 10;
                break;
        }

        return point;
    }

    /**
     * Accessor method which returns tileMapRenderer
     *
     * @return - tiledMapRenderer from Map class
     */
    public TiledMapRenderer getTiledMapRenderer() {
        return currentMap.getTiledMapRenderer();
    }

    /**
     * Check to see if a point is occupied by an NPC
     *
     * @param x - x position
     * @param y - y position
     * @return - true if point is occupied, false otherwise
     */
    public boolean occupied(int x, int y) {

        // check every spot in spots for a matching x,y position
        for (int i = 0; i < spots.size(); i++) {
            if (x == spots.get(i).x && y == spots.get(i).y) {
                return true;
            }
        }
        return false;
    }

    public boolean occupied(Point p) {

        for (int i = 0; i < spots.size(); i++) {
            if (p.x == spots.get(i).x && p.y == spots.get(i).y) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks spot that player is facing
     *
     * @param x         - x coordinate of player
     * @param y         - y coordinate of player
     * @param direction - direction that player is facing
     * @return
     */
    public NPC checkSpot(int x, int y, int direction) {

        Point p = null;

        // check point depending upon player's direction
        switch (direction) {
            case FORWARD:
                p = new Point(x, y + 1);
                break;
            case LEFT:
                p = new Point(x - 1, y);
                break;
            case RIGHT:
                p = new Point(x + 1, y);
                break;
            case BACKWARD:
                p = new Point(x, y - 1);
                break;
        }

        // return NPC at new point (if exists) else return null
        for (int i = 0; i < spots.size(); i++) {
            if (spots.get(i).point.equals(p)) {
                return spots.get(i).npc;
            }
        }
        return (NPC) null;
    }

}

/* Spot class is used to hold information about an NPC at a point */
class Spot {
    public NPC npc;
    public Point point;
    public int x;
    public int y;

    Spot(Point p, NPC npc) {
        this.npc = npc;
        this.point = p;
        x = p.x;
        y = p.y;
    }
}