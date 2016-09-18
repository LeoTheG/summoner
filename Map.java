package gharib.leonar.summoner;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/* Author       : Leonar Gharib
   Date Created : 6/18/2015
   Purpose      : Map allows construction of all Map classes used in Boxmon. These classes hold the information about
                  tiles to be rendered, the file name, ID, width, height, etc.
*/
public class Map {

    private String fileName;

    private int ID;
    private int width;
    private int height;

    private TiledMapRenderer tiledMapRenderer;
    private TiledMapTileLayer tiledMapTileLayer;

    private TiledMap tiledMap;

    /**
     * Constructor for Map
     *
     * @param fileName - name of file to load
     * @param ID       - ID of Map
     */
    public Map(String fileName, int ID) {
        this.fileName = fileName;
        this.ID = ID;

        tiledMap = new TmxMapLoader().load("data/" + this.fileName);

        tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        width = tiledMapTileLayer.getWidth();
        height = tiledMapTileLayer.getHeight();
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    /**
     * Accessor method for tileMapRenderer
     *
     * @return - tiledMapRenderer
     */
    public TiledMapRenderer getTiledMapRenderer() {
        return tiledMapRenderer;
    }

    /**
     * Updates tiledMapRenderer with new tiledMap
     */
    public void updateTiledMapRenderer() {
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    /**
     * Gets tile's "Name" property at certain point
     *
     * @param x - x position of tile
     * @param y - y position of tile
     * @return - String value of "Name" property or NULL if property doesn't exist on certain tile
     */
    public String getTileName(int x, int y) {

        TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(x, y);

        return (String) cell.getTile().getProperties().get("Name");
    }

    /**
     * Gets mapID that door at certain point switches to
     *
     * @param x - x position of door
     * @param y - y position of door
     * @return - mapID that door switches map to upon entering door or -1 if tile is not a door
     */
    public int getDoorID(int x, int y) {
        TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(x, y);
        int mapID = -1;

        // if "Door" property exists then tile is door therefore check mapID to see where door leads
        if (cell.getTile().getProperties().get("Door") != null) {
            mapID = Integer.parseInt((String) cell.getTile().getProperties().get("mapID"));
        }
        return mapID;
    }

    public int getObjectID(int x, int y) {
        TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(x, y);

        MapLayers mp = tiledMap.getLayers();
        int numLayers = mp.getCount();

        // safety check that there is an object layer (only layer with tiles with property "Passable"
        if (numLayers == 1){
            System.err.println("Only 1 layer");
            return -1;
        }

        TiledMapTileLayer tiledMapTileLayerObject = (TiledMapTileLayer) tiledMap.getLayers().get(1);

        TiledMapTileLayer.Cell cellObject = tiledMapTileLayerObject.getCell(x, y);

        System.err.println("Checking x, y at " + x + ", " + y);

        int ID = -1;

        if (cellObject != null) {
            System.err.println("Object is not null");
            if( cellObject.getTile().getProperties().get("Object") != null){
                System.err.println("Object is not null");
                ID = Integer.parseInt((String) cellObject.getTile().getProperties().get("ID"));
                System.err.println("Got ID");
            }
        }

        return ID;
    }

    /**
     * Gets "Passable" property of tile at certain point
     *
     * @param x - x position of tile
     * @param y - y position if tile
     * @return - NULL if tile passable, String "0" if tile is not passable
     */
    public String getPassable(int x, int y) {
        MapLayers mp = tiledMap.getLayers();
        int numLayers = mp.getCount();

        // safety check that there is an object layer (only layer with tiles with property "Passable"
        if (numLayers == 1) return null;

        TiledMapTileLayer tiledMapTileLayerObject = (TiledMapTileLayer) tiledMap.getLayers().get(1);

        TiledMapTileLayer.Cell cellObject = tiledMapTileLayerObject.getCell(x, y);
        String passable = null;

        // if property "Passable" exists then object is not passable
        if (cellObject != null) {
            if (cellObject != null && cellObject.getTile().getProperties().get("Passable") == null)
                return null;

            else passable = (String) cellObject.getTile().getProperties().get("Passable");
        }
        return passable;
    }
}
