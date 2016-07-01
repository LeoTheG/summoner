package gharib.leonar.summoner;

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


    public Map(String fileName, int ID)
    {
        this.fileName = fileName;
        this.ID = ID;

        TiledMap tiledMap = new TmxMapLoader().load(this.fileName);

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

    public String getTileName(int x, int y){

        System.err.println("--Checking spot: " + x + ", " + y );

        TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(x,y);

        return (String) cell.getTile().getProperties().get("Name");
    }
}
