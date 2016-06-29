package gharib.leonar.summoner;

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
    public List<Point> points = new ArrayList<Point>();
    private NPCParser npcParser;

    private Map[] maps = { new Map("maps\\house2.tmx", 0)

                         };
    private NPC[] npcs;

    public World(NPCParser npcParser)
    {
        this.npcParser = npcParser;
        currentMapIndex = 0;
        currentMap = maps[currentMapIndex]; //begin inside map house1
        npcParser.parse();
        npcs = npcParser.npcs;
        points.add( new Point( npcs[0].x, npcs[0].y ));
    }
    public Map getCurrentMap()
    {
        return currentMap;
    }
    public int getCurrentMapIndex() { return currentMapIndex; }
    public void changeMap(String flag)
    {
        /*
        if ( flag.equals("map1_entrance"))
        {
            //currentMap = new Map(new File("..\\assets\\house1.map"));
        }
        */
    }
    public TiledMapRenderer getTiledMapRenderer()
    {
        return currentMap.getTiledMapRenderer();
    }

    public boolean occupied(Point p){
        for ( int i = 0; i < points.size(); i++ ) {
            if ( p.x == points.get(i).x && p.y == points.get(i).y) return true;
        }
        return false;
    }

}
