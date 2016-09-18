package gharib.leonar.summoner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

/* Author       : Leonar Gharib
   Date Created : 6/29/2016
   Purpose      : Handle menu sprites and menu navigation. Uses states as control points
                  for exploring menu options.
*/
public class MenuHandler {

    // FREE - player is not in any menu
    // CHATTING - player has text currently displaying on his screen
    // INCHAT - player has finished text displayed on his screen (CHATTING progresses to INCHAT)
    // MAINMENU - player is in main menu where they can choose multiple options
    public enum states {
        FREE, CHATTING, INCHAT, MAINMENU, BATTLE, PACK
    }

    private states s;
    private ArrayList<Sprite> sprites;

    private static int TILE_LENGTH = 64;
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 1280;

    private String currentChat = "";

    private Sprite textBox = new Sprite(new Texture(Gdx.files.internal("data/UI/gray-box.png")));
    private Sprite mainMenuBox = new Sprite(new Texture(Gdx.files.internal("data/UI/menu-box.png")));
    private Sprite cursor = new Sprite(new Texture(Gdx.files.internal("data/UI/cursor.png")));
    private Sprite cursorBattle = new Sprite(new Texture(Gdx.files.internal("data/UI/cursor.png")));

    private Player player;

    private int[] keysPressed = new int[4]; // for up, down, left, right key movement

    private static int UP = 0;
    private static int DOWN = 1;
    private static int LEFT = 2;
    private static int RIGHT = 3;

    private Point cursorPoint;
    private int cursorSelection;

    private static final int CURSOR_OFFSET_X = 130;
    private static final int CURSOR_OFFSET_Y = 260;
    private static final int CURSOR_SELEC_Y = 36;

    // cursor offset in battle main text box
    private static int CURSOR_OFFSET_X_BATTLE_MAIN = -303;
    private static int CURSOR_OFFSET_Y_BATTLE_MAIN = -217;
    // offset to cursor offset for different cursor positions
    private static int CURSOR_SELEC_Y_BATTLE_MAIN = -98;
    private static int CURSOR_SELEC_X_BATTLE_MAIN = 377;
    // battle main text
    private static int BATTLE_MAIN_XOFFSET = -250;
    private static int BATTLE_MAIN_YOFFSET = -170;
    // enemy sprite and player sprite offsets
    private static int MONSTER_PLAYER_SPRITE_XOFFSET = 136;
    private static int MONSTER_PLAYER_SPRITE_YOFFSET = 45;
    private static int MONSTER_ENEMY_SPRITE_XOFFSET = -200;
    private static int MONSTER_ENEMY_SPRITE_YOFFSET = 45;

    private Spirit spirit1 = new Spirit(10,50,30,5,5,100);
    private Spirit spirit2 = new Spirit(10,50,30,5,5,100);

    private boolean newChat;
    private boolean pressedSpace;
    private boolean pressedQ = false;
    private boolean pressedE = false;
    private boolean pressedZ = false;
    private boolean pressedX = false;

    private Preferences pref;

    private World world;


    private int previousMapIndex = 0;
    private boolean flagMapChange = false;

    private Sprite[] monsterSprites = new Sprite[2];

    private Point previousPoint;

    private BattleHandler battleHandler;

    /** Constructor which begins states as FREE */
    public MenuHandler() {

        s = states.FREE;
        sprites = new ArrayList<Sprite>();

        // properly scale menu items
        textBox.setScale(6.5f, 3.5f);
        mainMenuBox.setScale(3.5f, 2f);
        cursor.setScale(0.2f, 0.2f);
        cursorBattle.setScale(0.5f,0.5f);

        cursorPoint = new Point(0, 0);
        cursorSelection = 0;

        newChat = true;

        spirit1.setName("player");
        spirit2.setName("enemy");

        pressedSpace = false;

        previousPoint = new Point(0,0);
    }

    /**
     * Mutator method for setting player
     *
     * @param p - player object
     */
    public void setPlayer(Player p) {
        player = p;
    }
    public void setNewChat(boolean b){
        newChat = b;
    }
    public boolean getNewChat(){
        return newChat;
    }
    public void setBattleHandler (BattleHandler b){ battleHandler = b;}
    public void setPreferences(Preferences p){
        pref = p;
    }
    public void setWorld(World w){
        world = w;
    }

    public int getPreviousMapIndex(){
        return previousMapIndex;
    }

    public boolean getFlagMapChange(){
        return flagMapChange;
    }
    public void setFlagMapChange(boolean b){
        flagMapChange = b;
    }
    public  void setPreviousMapIndex(int i){
        previousMapIndex = i;
    }

    /**
     * Change menu when SPACE is pressed depending on menu state
     * Also used for interacting with objects in world (on tile layer 2)
     */
    public void pressedSpace(NPC npc) {

        BattleHandler.battleStates bs = battleHandler.getBattleState();

        int playerX = player.getMapX();
        int playerY = player.getMapY();
        Player.directions playerDirection = player.getDirection();

        int adjustedX = playerX, adjustedY = playerY;

        switch ( playerDirection ) {
            case FORWARD:
                adjustedY += 1;
                break;
            case RIGHT:
                adjustedX += 1;
                break;
            case LEFT:
                adjustedX -= 1;
                break;
            case BACKWARD:
                adjustedY -= 1;
                break;
        }

        int objectID = world.getCurrentMap().getObjectID(adjustedX, adjustedY);

        // IDs > -1 indicate object
        if ( objectID > -1 ) {
            player.getItem(objectID);
            return;
        }

        if (npc == null) {
            setChat("");
            return;
        }

        if (s == states.FREE) {

            setChat(npc.getChat(true));
            if (currentChat.length() == 0) return;
            sprites.add(textBox);
            s = states.CHATTING;
            setNewChat(true);

        }
        else if (s == states.CHATTING) {
            s = states.INCHAT;
            setNewChat(false);

        }
        else if (s == states.INCHAT) {

            // if NPC can battle, then initiate battle after end of chat
            if ( npc.finishedChat() ) {

                if ( npc.getBattle() ) {

                    System.err.println("Entering battle");
                    newChat = true;

                    s = states.BATTLE;
                    cursorSelection = 0;
                    battleHandler.begin(spirit1, spirit2, npc);

                    monsterSprites = battleHandler.getSprites();

                    monsterSprites[0].setX(10 * TILE_LENGTH + MONSTER_PLAYER_SPRITE_XOFFSET);
                    monsterSprites[0].setY(10 * TILE_LENGTH + MONSTER_PLAYER_SPRITE_YOFFSET);

                    monsterSprites[1].setX(10 * TILE_LENGTH + MONSTER_ENEMY_SPRITE_XOFFSET);
                    monsterSprites[1].setY(10 * TILE_LENGTH + MONSTER_ENEMY_SPRITE_YOFFSET);

                    sprites.add(monsterSprites[0]);
                    sprites.add(monsterSprites[1]);

                    flagMapChange = true;
                    previousPoint.x = player.getX();
                    previousPoint.y = player.getY();
                }
                else {

                    sprites.remove(textBox);
                    newChat = true;

                    s = states.FREE;
                }

            }
            else{
                setChat(npc.getChat(true));
                s = states.CHATTING;
                newChat = true;
            }

        }

    }

    /**
     * Change menu when F is pressed depending on menu state
     */
    public void pressedF() {
        if (s == states.FREE) {
            sprites.add(mainMenuBox);
            sprites.add(cursor);
            s = states.MAINMENU;
            currentChat = "PACK\nSPIRITS\nBOOK\nSAVE";
        } else if (s == states.MAINMENU) {
            s = states.FREE;
            sprites.remove(mainMenuBox);
            sprites.remove(cursor);
            cursorSelection = 0;
        }

    }

    /**
     * Mutator method for state
     *
     * @param st - state to change menuHandler's state to
     */
    public void setState(states st) {
        s = st;
    }

    /**
     * Getter method for state
     *
     * @return - state of menuHandler
     */
    public states getState() {
        return s;
    }

    /**
     * Returns menu sprites; used to draw sprites in SummonerGame class
     *
     * @return - array of sprites to draw
     */
    public Sprite[] getSprites() {
        // convert sprites list to array
        Sprite[] arr = new Sprite[sprites.size()];
        Sprite[] arrs = sprites.toArray(arr);

        // depending on menu sprites being displayed, adjust X and Y positions
        for (int i = 0; i < arrs.length; i++) {
            if (arrs[i] == textBox) {
                textBox.setX(player.getX() - textBox.getWidth() / 2);
                textBox.setY(player.getY() - HEIGHT / 4 + 60);
            } else if (arrs[i] == mainMenuBox) {
                mainMenuBox.setX(player.getX() + mainMenuBox.getWidth() / 2 + 182);
                mainMenuBox.setY(player.getY() + 170);
            } else if (arrs[i] == cursor) {
                cursor.setX(cursorPoint.x);
                cursor.setY(cursorPoint.y);
            }
            else if ( arrs[i] == cursorBattle ) {
                cursorBattle.setX(cursorPoint.x);
                cursorBattle.setY(cursorPoint.y);
            }
        }

        return sprites.toArray(arr);
    }

    /**
     * Getter method for currentChat
     */
    public String getChat() {
        return currentChat;
    }

    /**
     * Setter method for currentChat
     */
    public void setChat(String str) {

        if ( str == null ) currentChat = "";

        currentChat = str;
    }

    public void drawBattleMainText(Batch b, BitmapFont f, BitmapFont grayFont){

        for( int i = 0; i < battleHandler.getMainText().length; i++ ) {

            int x = player.getX() + BATTLE_MAIN_XOFFSET;
            int y = player.getY() + BATTLE_MAIN_YOFFSET;

            if ( battleHandler.getFlagEndBattle() ) {
                f.draw(b, battleHandler.getMainText()[0], x, y);
            }
            else {

                if (i == 1 || i == 3) x += 380;
                if (i == 2 || i == 3) y += -97;

                if (i == 0) {
                    if (battleHandler.getPlayerEnergy() < battleHandler.getPlayerAtkCost()) {
                        grayFont.draw(b, battleHandler.getMainText()[0], x, y);
                    } else {
                        f.draw(b, battleHandler.getMainText()[0], x, y);
                    }
                }
                else if ( i == 2 ) {
                    if ( player.getBag().getNumItems() > 0 ) {
                        f.draw(b, battleHandler.getMainText()[2], x, y);
                    }
                    else{
                        grayFont.draw(b, battleHandler.getMainText()[2], x, y);
                    }
                }
                // RUN should be grayed out at certain times
                else
                    f.draw(b, battleHandler.getMainText()[i], x, y);

            }


        }

    }
    public void drawBattleSpellText(Batch b, BitmapFont f, BitmapFont grayFont){
        for( int i = 0; i < battleHandler.getSpellText().length; i++ ) {

            int x = player.getX() + BATTLE_MAIN_XOFFSET;
            int y = player.getY() + BATTLE_MAIN_YOFFSET;

            if ( i == 1 || i == 3 ) x += 380;
            if ( i == 2 || i == 3 ) y += -97;

            if ( battleHandler.playerCanCast(i) )
                f.draw(b, battleHandler.getSpellText()[i], x, y);
            else
                grayFont.draw(b, battleHandler.getSpellText()[i], x, y);


        }
    }
    public Point getPreviousPoint(){
        return previousPoint;
    }

    /**
     * Handles menu and cursor depending on keyboard input
     */
    public void menuHandle() {

        BattleHandler.battleStates bs = battleHandler.getBattleState();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if ( !pressedSpace ) {

                pressedSpace = true;

                // SAVE option
                if ( getState() == states.MAINMENU ) {

                    // PACK
                    if ( cursorSelection == 0 ) {
                        s = states.PACK;
                    }
                    // SPIRITS
                    else if ( cursorSelection == 1) {

                    }

                    else if ( cursorSelection == 3 ) {
                        System.err.println("Saving game");
                        pref.putInteger("playerX", player.getX());
                        pref.putInteger("playerY", player.getY());
                        pref.putInteger("playerDirection", player.getDirectionID());
                        pref.putInteger("mapID", world.getCurrentMapIndex());

                        s = states.FREE;
                        sprites.remove(mainMenuBox);
                        sprites.remove(cursor);
                    }
                }

                if ( bs == BattleHandler.battleStates.BEG ){
                    setNewChat(false);
                    battleHandler.setBattleState(BattleHandler.battleStates.BEG_COMP);
                }
                if ( bs == BattleHandler.battleStates.BEG_COMP ) {
                    setNewChat(false);
                    System.err.println("Pressed space, switching to main");
                    battleHandler.setBattleState(BattleHandler.battleStates.MAIN);

                    sprites.add(cursorBattle);
                }
                if ( bs == BattleHandler.battleStates.MAIN ) {

                    // player chose ATK
                    if ( cursorSelection == 0 ) {
                        battleHandler.attack(false);
                    }
                    // MAG
                    else if ( cursorSelection == 1 ) {
                        battleHandler.setBattleState(BattleHandler.battleStates.SPELL);
                        cursorSelection = 0;
                    }
                    // PACK
                    else if ( cursorSelection == 2 ) {
                        if ( player.getBag().getNumItems() > 0 )
                            battleHandler.setBattleState(BattleHandler.battleStates.PACK);
                    }
                    // RUN
                    else if ( cursorSelection == 3 ) {
                        if (battleHandler.run()) {
                            battleHandler.setBattleState(BattleHandler.battleStates.FREE);
                            setState(states.FREE);
                            sprites.remove(monsterSprites[0]);
                            sprites.remove(monsterSprites[1]);
                            sprites.remove(cursorBattle);
                            sprites.remove(textBox);
                        }
                    }
                }
                else if ( bs == BattleHandler.battleStates.SPELL ) {
                    if ( battleHandler.castSpell(cursorSelection) )
                        battleHandler.setBattleState(BattleHandler.battleStates.MAIN);
                }

                if ( battleHandler.getFlagEndBattle() ) {
                    monsterSprites = battleHandler.getSprites();
                    sprites.remove(cursorBattle);
                    battleHandler.setBattleState(BattleHandler.battleStates.END);

                }
                if ( bs == BattleHandler.battleStates.END ) {
                    monsterSprites = battleHandler.getSprites();

                    battleHandler.setBattleState(BattleHandler.battleStates.FREE);
                    setState(states.FREE);

                    sprites.remove(monsterSprites[0]);
                    sprites.remove(monsterSprites[1]);

                    sprites.remove(textBox);

                    battleHandler.setFlagEndBattle(false);
                    battleHandler.getEnemyNPC().setDefeated(true);
                }
            }

        }
         else pressedSpace = false;

        // handle cursor selection in main menu
        if (s == states.MAINMENU) {

            // UP
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {

                if (keysPressed[UP] == 0) {
                    keysPressed[UP] = 1;

                    if (cursorSelection != 0) {
                        cursorSelection--;
                        System.err.println("Reducing cursorSelection to " + cursorSelection);
                    }
                }
            } else keysPressed[UP] = 0;

            // DOWN
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {

                if (keysPressed[DOWN] == 0) {
                    keysPressed[DOWN] = 1;

                    if (cursorSelection != 3) {
                        cursorSelection++;
                        System.err.println("Increasing cursorSelection to " + cursorSelection);
                    }
                }
            } else keysPressed[DOWN] = 0;

            // set cursor point
            cursorPoint.x = player.getX() + CURSOR_OFFSET_X;
            cursorPoint.y = player.getY() + CURSOR_OFFSET_Y - (CURSOR_SELEC_Y * cursorSelection);

        }
        // handle cursor selection in case of main battle menu
        else if ( battleHandler.getBattleState() == BattleHandler.battleStates.MAIN ) {
            // UP
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {

                if (keysPressed[UP] == 0) {
                    keysPressed[UP] = 1;

                    if ( cursorSelection > 1 ) cursorSelection -= 2;

                }
            } else keysPressed[UP] = 0;

            // DOWN
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {

                if (keysPressed[DOWN] == 0) {
                    keysPressed[DOWN] = 1;

                    if ( cursorSelection < 2 ) cursorSelection += 2;

                }
            } else keysPressed[DOWN] = 0;

            // LEFT
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {

                if (keysPressed[LEFT] == 0) {
                    keysPressed[LEFT] = 1;

                    if ( cursorSelection == 1 || cursorSelection == 3 ) cursorSelection--;

                }
            } else keysPressed[LEFT] = 0;

            // RIGHT
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {

                if (keysPressed[RIGHT] == 0) {
                    keysPressed[RIGHT] = 1;

                    if ( cursorSelection == 0 || cursorSelection == 2 ) cursorSelection++;

                }
            } else keysPressed[RIGHT] = 0;

            // set cursor point
            cursorPoint.x = player.getX() + CURSOR_OFFSET_X_BATTLE_MAIN;
            cursorPoint.y = player.getY() + CURSOR_OFFSET_Y_BATTLE_MAIN;

            if ( cursorSelection == 1 || cursorSelection == 3 ) cursorPoint.x += CURSOR_SELEC_X_BATTLE_MAIN;
            if ( cursorSelection == 2 || cursorSelection == 3 ) cursorPoint.y += CURSOR_SELEC_Y_BATTLE_MAIN;


        }
        else if ( battleHandler.getBattleState() == BattleHandler.battleStates.SPELL ) {

            int tempChoice = cursorSelection;

            // UP
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {

                if (keysPressed[UP] == 0) {
                    keysPressed[UP] = 1;

                    tempChoice -= 2;

                }
            } else keysPressed[UP] = 0;

            // DOWN
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {

                if (keysPressed[DOWN] == 0) {
                    keysPressed[DOWN] = 1;

                    tempChoice += 2;

                }
            } else keysPressed[DOWN] = 0;

            // LEFT
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {

                if (keysPressed[LEFT] == 0) {
                    keysPressed[LEFT] = 1;

                    tempChoice -= 1;

                }
            } else keysPressed[LEFT] = 0;

            // RIGHT
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {

                if (keysPressed[RIGHT] == 0) {
                    keysPressed[RIGHT] = 1;

                    tempChoice += 1;

                }
            } else keysPressed[RIGHT] = 0;

            if ( tempChoice >= 0 && tempChoice < player.getSpells().size() )
                cursorSelection = tempChoice;

            // set cursor point
            cursorPoint.x = player.getX() + CURSOR_OFFSET_X_BATTLE_MAIN;
            cursorPoint.y = player.getY() + CURSOR_OFFSET_Y_BATTLE_MAIN;

            if ( cursorSelection == 1 || cursorSelection == 3 ) cursorPoint.x += CURSOR_SELEC_X_BATTLE_MAIN;
            if ( cursorSelection == 2 || cursorSelection == 3 ) cursorPoint.y += CURSOR_SELEC_Y_BATTLE_MAIN;


        }

    }
}
