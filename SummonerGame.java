package gharib.leonar.summoner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;

/* Author       : Leonar Gharib
   Date Created : 6/15/2015
   Purpose      : BoxmonGame is the main handler for everything game related. It creates and renders the World, the
   				  player, NPCs, etc.
*/

public class SummonerGame extends ApplicationAdapter {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 1280;

	private SpriteBatch batch;
	private Player player;
	private World world;
	private OrthographicCamera camera;

	private TiledMapRenderer tiledMapRenderer;

	private Preferences prefs;

	private NPCParser npcParser;

	private NPC[] npcs;

	private String fontName = "UI\\fonts\\ARCADECLASSIC.TTF";
	private FreeTypeFontGenerator generator;
	private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
	private BitmapFont font;

	private NPCHandler npcHandler;
	private MenuHandler menuHandler;

	private String text = "ABCD EFGH IJKL MNOP QRST UVWX YZ";

	@Override
	public void create () {

		FileHandle fh = new FileHandle(fontName);
		generator = new FreeTypeFontGenerator(fh);
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 30;
		font = generator.generateFont(parameter);

		menuHandler = new MenuHandler();


		prefs = Gdx.app.getPreferences("BoxmonSave");
		batch = new SpriteBatch();
		npcParser = new NPCParser();
		npcParser.parse();
		npcs = npcParser.npcs;
		world = new World(npcParser);
		camera = new OrthographicCamera(WIDTH, HEIGHT);
		tiledMapRenderer = world.getTiledMapRenderer();
		player = new Player(prefs, world, menuHandler);
		camera.zoom = (float)0.5;

		npcHandler = new NPCHandler(batch);

		menuHandler.setPlayer(player);
		font.setColor(0f,0f,0f,1f);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//camera
		camera.position.set(player.getX(), player.getY(), 0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		//end camera handling

		player.moveHandler();

		//tiledMap actions
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		//begin rendering
		batch.begin();
		if ( player.getY() < npcs[0].getSprite().getY() ) {
			npcs[0].getSprite().draw(batch);
			player.getPlayerSprite().draw(batch);
		}
		else{
			player.getPlayerSprite().draw(batch);
			npcs[0].getSprite().draw(batch);
		}

		Sprite[] sprites = menuHandler.getSprites();

		for(int i = 0; i < sprites.length; i++){
			sprites[i].draw(batch);
		}
		if ( menuHandler.getState() == MenuHandler.states.INCHAT) {
			text = menuHandler.getChat();
			for (int i = 0; i < text.length(); i++) {
				font.draw(batch, "" + text.charAt(i), player.getX() - 300 + (i * 15), player.getY() - 160);
			}
		}
		batch.end();
	}
	public void dispose()
	{
		player.fixXY();
		prefs.putInteger("playerX", player.getX());
		prefs.putInteger("playerY", player.getY());
		prefs.putInteger("playerDirection", player.getDirection());
		prefs.flush();
		generator.dispose();
	}
}
