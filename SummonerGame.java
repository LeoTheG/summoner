package gharib.leonar.summoner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;

/* Author       : Leonar Gharib
   Date Created : 6/15/2015
   Purpose      : BoxmonGame is the main handler for everything game related. It creates and renders the World, the
   				  player, NPCs, etc.
*/

public class SummonerGame extends ApplicationAdapter {

    // Game dimensions
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 1280;

    private static final int CHATOFFSET_X = -291;
    private static final int CHATOFFSET_Y = -160;

    private static final int MAINMENUOFFSET_X = 170;
    private static final int MAINMENUOFFSET_Y = 300;

    private SpriteBatch batch;
    private Player player;
    private World world;

    private OrthographicCamera camera;

    private TiledMapRenderer tiledMapRenderer;

    private Preferences prefs;

    private NPCParser npcParser;

    // for drawing text on the screen
    private String fontName = "data/UI/fonts/prstart.ttf";
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont font; // normal text font
    private BitmapFont mmFont; // main menu font (smaller)
    private BitmapFont grayFont;
    private static final int CHAT_SIZE = 30;
    private static final int MENU_SIZE = 16;
    private String text;
    public float textDrawLength = 0.0f;
    public static float TEXTSPEED = 0.5f;

    private NPCHandler npcHandler;
    private MenuHandler menuHandler;

    private BattleHandler battleHandler;

    @Override
    public void create() {

        shapeRenderer = new ShapeRenderer();

        // creates normal text font
        text = "";
        generator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = CHAT_SIZE;
        parameter.spaceX = 4;
        parameter.borderColor = Color.BLACK;
        parameter.kerning = true;

        font = generator.generateFont(parameter);

        parameter.borderColor = Color.GRAY;

        grayFont = generator.generateFont(parameter);

        // creates smaller menu font
        parameter.borderColor = Color.BLACK;
        parameter.size = MENU_SIZE;
        parameter.spaceY = 20;
        mmFont = generator.generateFont(parameter);

        // set fonts to black and fully visible
        font.setColor(0f, 0f, 0f, 1f);
        mmFont.setColor(0f, 0f, 0f, 1f);

        menuHandler = new MenuHandler();

        // loads latest save
        prefs = Gdx.app.getPreferences("BoxmonSave");

        batch = new SpriteBatch();

        npcParser = new NPCParser();

        world = new World(prefs, npcParser);
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        tiledMapRenderer = world.getTiledMapRenderer();
        player = new Player(prefs, world, menuHandler);
        camera.zoom = (float) 0.5;

        // gives world object access to NPCHandler and menuHandler access to player
        battleHandler = new BattleHandler();
        npcHandler = new NPCHandler(batch);
        world.setNPCHandler(npcHandler);
        menuHandler.setPlayer(player);
        menuHandler.setBattleHandler(battleHandler);



        //battleHandler.begin(spirit1, spirit2);

    }

    @Override
    public void render() {

        MenuHandler.states menuState = menuHandler.getState();
        BattleHandler.battleStates battleState = battleHandler.getBattleState();

        if ( battleState != BattleHandler.battleStates.FREE ) {
            battleHandler.handle();
        }

        // clear background for proper rendering
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        tiledMapRenderer = world.getTiledMapRenderer();

        //camera
        camera.position.set(player.getX(), player.getY(), 0);
        //System.err.println("Setting camera position to " + player.getX() + ", " + player.getY());
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // handles player's movement (keyboard input and all)
        if ( battleState == BattleHandler.battleStates.FREE )
            player.moveHandler();

        // handles main menu if player is in main menu
        if (menuHandler.getState() != MenuHandler.states.FREE) {
            menuHandler.menuHandle();
            if ( menuHandler.getFlagMapChange() ) {
                menuHandler.setPreviousMapIndex(world.getCurrentMapIndex());
                //world.changeMap(2);
                player.changeMapSetCoords(2);
                menuHandler.setFlagMapChange(false);
            }
        }
        else if ( world.getCurrentMapIndex() == 2 ) {
            //world.changeMap(menuHandler.getPreviousMapIndex());
            player.changeMapSetCoords(menuHandler.getPreviousMapIndex(), menuHandler.getPreviousPoint().x, menuHandler.getPreviousPoint().y);
        }

        //tiledMap actions
        tiledMapRenderer.setView(camera);
        //if ( battleState == BattleHandler.battleStates.FREE )
        tiledMapRenderer.render();

        //begin rendering
        batch.begin();

        // for drawing NPCs and player (according to Y value)
        if ( battleState == BattleHandler.battleStates.FREE )
        npcHandler.render(player);



        // for drawing menu
        if (menuHandler.getState() != MenuHandler.states.FREE) {

            Sprite[] sprites = menuHandler.getSprites();

            for (int i = 0; i < sprites.length; i++) {
                sprites[i].draw(batch);
            }


        }

        //System.err.println("textDrawLength = "  + textDrawLength);

        // for drawing chat one character at a time
        if (menuState == MenuHandler.states.CHATTING) {

            text = menuHandler.getChat(); // get text from menuHandler which pulls from NPC

            if ((int) textDrawLength < text.length()) {
                textDrawLength += (0.5f * TEXTSPEED);
            }
            else {
                menuHandler.setState(MenuHandler.states.INCHAT);
            }

            font.draw(batch, text.substring(0, (int) textDrawLength), player.getX() + CHATOFFSET_X, player.getY() + CHATOFFSET_Y);
        }
        // display full text in case of INCHAT
        else if (menuState == MenuHandler.states.INCHAT) {


            if ( !menuHandler.getNewChat() ) {

                text = menuHandler.getChat();

                textDrawLength = text.length();

                font.draw(batch, text.substring(0, (int) textDrawLength), player.getX() + CHATOFFSET_X, player.getY() + CHATOFFSET_Y);
            }
            else {
                menuHandler.setNewChat(false);
                System.err.println("Setting new chat to false");
                textDrawLength = 0;
            }

        }
        // display full text in case of menu
        else if (menuState == MenuHandler.states.MAINMENU) {
            text = menuHandler.getChat();
            textDrawLength = text.length();
            mmFont.draw(batch, text.substring(0, (int) textDrawLength), player.getX() + MAINMENUOFFSET_X, player.getY() + MAINMENUOFFSET_Y);
        }
        // draw one character at a time for beginning battle text
        else if (battleState == BattleHandler.battleStates.BEG ){


            if ( menuHandler.getNewChat() ) {
                menuHandler.setNewChat(false);
                System.err.println("Setting drawlength to 0");
                textDrawLength = 0;
            }

            text = battleHandler.getChat();

            if ((int) textDrawLength < text.length()) {
                textDrawLength += (0.5f * TEXTSPEED);
            }
            else battleHandler.setBattleState(BattleHandler.battleStates.BEG_COMP);

            font.draw(batch, text.substring(0, (int) textDrawLength), player.getX() + CHATOFFSET_X, player.getY() + CHATOFFSET_Y);

        }
        // draw complete text in beginning
        else if ( battleState == BattleHandler.battleStates.BEG_COMP){
            text = battleHandler.getChat();

            textDrawLength = text.length();

            font.draw(batch, text.substring(0, (int) textDrawLength), player.getX() + CHATOFFSET_X, player.getY() + CHATOFFSET_Y);

        }
        // draw main battle menu text
        else if ( battleState == BattleHandler.battleStates.MAIN ) {

            // draw main text
            menuHandler.drawBattleMainText(batch,font, grayFont);

            battleHandler.tick();

        }
        // draw no text if player is not in menu or chatting
        else if (menuHandler.getState() == MenuHandler.states.FREE) {
            textDrawLength = 0;
        }

        if ( menuState == MenuHandler.states.BATTLE ) {
            battleHandler.renderBars(batch);

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        batch.end();


    }

    public void dispose() {
        player.fixXY();
        // save player's x,y position, their direction, and the map they are on
        prefs.putInteger("playerX", player.getX());
        prefs.putInteger("playerY", player.getY());
        prefs.putInteger("playerDirection", player.getDirection());
        prefs.putInteger("mapID", world.getCurrentMapIndex());
        prefs.flush();
        generator.dispose();
    }
}
