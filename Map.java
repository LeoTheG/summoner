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


    public Map(String fileName, int ID)
    {
        this.fileName = fileName;
        this.ID = ID;

        tiledMap = new TmxMapLoader().load(this.fileName);

        tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        width = tiledMapTileLayer.getWidth();
        height = tiledMapTileLayer.getHeight();
    }
    public int getID()
    {
        return ID;
    }
    public int[] getDimensions()
    {
        int[] dimensions = {width, height};
        return dimensions;
    }
    public TiledMapRenderer getTiledMapRenderer()
    {
        return tiledMapRenderer;
    }

    public void updateTiledMapRenderer(){
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public String getTileName(int x, int y){

        //System.err.println("--Checking spot: " + x + ", " + y );
        //System.err.println("Checking cell " + x + ", " + y );
        TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(x,y);

        return (String) cell.getTile().getProperties().get("Name");
    }
    // gets door's ID
    public int getDoorID(int x, int y){
        TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(x,y);
        int mapID = -1;
        String door = null;

        // if "Door" property exists then tile is door therefore check mapID to see where door leads
        if ( cell.getTile().getProperties().get("Door") != null ){
            mapID = Integer.parseInt((String)cell.getTile().getProperties().get("mapID"));
        }
        return mapID;

    }
    public String getPassable(int x, int y){
        MapLayers mp = tiledMap.getLayers();
        int numLayers = mp.getCount();

        // safety check that there is an object layer
        if ( numLayers == 1 ) return null;

        TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(x,y);

        TiledMapTileLayer tiledMapTileLayerObject = (TiledMapTileLayer) tiledMap.getLayers().get(1);

        TiledMapTileLayer.Cell cellObject = tiledMapTileLayerObject.getCell(x,y);
        String passable = null;

        if ( cellObject != null ) {
            if (cellObject != null && cellObject.getTile().getProperties().get("Passable") == null)
                return null;

            else passable = (String) cellObject.getTile().getProperties().get("Passable");
        }
        return passable;
    }
}
