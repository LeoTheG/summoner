package gharib.leonar.summoner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

/* Author       : Leonar Gharib
   Date Created : 6/15/2015
   Purpose      : Player handles everything related to the player character including movement and key actions and
                  holds information about the character such as its (x, y) coordinates, its direction, and speed.
*/
public class Player {

    private static final int KEY_SENSITIVITY = 7; //how "hard" (long) the movement keys (WASD) can be pressed without movement (just rotation if possible)
    private static final int TILE_LENGTH = 64; //the width and height of the game tiles (square tiles where width = height)
    private static final int SPEED = 4; //the player's movement speed
    private static final int FORWARD = 0, RIGHT = 1, BACKWARD = 2, LEFT = 3; //player's direction constants

    public enum directions { FORWARD, RIGHT, BACKWARD, LEFT }

    //player's (x, y) coordinates
    private int x;
    private int y;
    //direction player is facing (values are constants FORWARD, RIGHT, BACKWARD, LEFT)
    private int direction;
    //integers that measure whether or not the player is moving in the direction (0 if not moving, max value = TILE_LENGTH/2)
    private int moveCounterUp = 0, moveCounterDown = 0, moveCounterLeft = 0, moveCounterRight = 0;
    //used in conjunction with KEY_SENSITIVITY to tell how long the respective movement keys have been pressed
    private int numWKeyPressed, numDKeyPressed, numSKeyPressed, numAKeyPressed,
            numSPACEKeyPressed, numFKeyPressed;
    //whether or not the player is moving (move counters are all = 0 or not)
    public boolean isMoving;
    //texture & sprite of player
    private Texture[] textures;
    private Sprite playerSprites[];
    //Preferences is the method of saving and loading information about the game
    private Preferences prefs;
    //The World that everything resides
    private World world;

    private MenuHandler menuHandler;

    private Journal journal;
    private Bag bag;
    private directions direc;

    //private Spell

    /**
     * Constructor for Player, sets up initial variables
     *
     * @param p - the Preferences object of BoxmonGame (used for loading information about Player
     * @param m - menuHandler which draws menu and handles menu keyboard input; player gives information
     * @world w - World in which
     */
    public Player(Preferences p, World w, MenuHandler m, SpellParser s) {
        world = w;
        prefs = p;
        menuHandler = m;


        init(s);
    }

    /* Initializes certain variables, called in constructor */
    private void init(SpellParser s) {

        // load player's x and y values and direction
        x = prefs.getInteger("playerX", 1 * TILE_LENGTH);
        y = prefs.getInteger("playerY", 1 * TILE_LENGTH);

        System.err.println("Save x,y = " + x + ", " + y);
       // x = 64;
       //y = 64;
        direction = prefs.getInteger("playerDirection");

        // load direction
        switch ( direction ) {
            case FORWARD:
                direc = directions.FORWARD;
                break;
            case LEFT:
                direc = directions.LEFT;
                break;
            case RIGHT:
                direc = directions.RIGHT;
                break;
            case BACKWARD:
                direc = directions.BACKWARD;
                break;
        }

        System.err.println("World width/height = " + world.getCurrentMap().getWidth() + ", " + world.getCurrentMap().getHeight() );

        if ( (x/TILE_LENGTH) > world.getCurrentMap().getWidth() || x < 1 ) x = 1 * TILE_LENGTH;
        if ( (y/TILE_LENGTH) > world.getCurrentMap().getHeight() || y < 1) y = 1 * TILE_LENGTH;

        // load textures for player
        textures = new Texture[4];
        /*
        textures[FORWARD] = new Texture(new FileHandle(new File("characters\\PlayerTexture.png")));
        textures[BACKWARD] = new Texture(new FileHandle(new File("characters\\PlayerTextureBack.png")));
        textures[LEFT] = new Texture(new FileHandle(new File("characters\\PlayerTextureLeft.png")));
        textures[RIGHT] = new Texture(new FileHandle(new File("characters\\PlayerTextureRight.png")));
        */
        textures[FORWARD] = new Texture(Gdx.files.internal("data\\characters\\PlayerTexture.png"));
        textures[BACKWARD] = new Texture(Gdx.files.internal("data\\characters\\PlayerTextureBack.png"));
        textures[LEFT] = new Texture(Gdx.files.internal("data\\characters\\PlayerTextureLeft.png"));
        textures[RIGHT] = new Texture(Gdx.files.internal("data\\characters\\PlayerTextureRight.png"));

        isMoving = false;

        // create sprites for player
        playerSprites = new Sprite[4];
        playerSprites[FORWARD] = new Sprite(textures[BACKWARD]);
        playerSprites[BACKWARD] = new Sprite(textures[FORWARD]);
        playerSprites[LEFT] = new Sprite(textures[LEFT]);
        playerSprites[RIGHT] = new Sprite(textures[RIGHT]);

        journal = new Journal();
        bag = new Bag(this);

        setSpellParser(s);
        journal.addSpell(0);

    }
    public Bag getBag(){
        return bag;
    }

    public void addSpell(int ID){
        journal.addSpell(ID);
    }

    public ArrayList<Spell> getSpells(){
        return journal.getSpells();
    }

    /* Fixes the x & y  coordinate of Player if Player is not exactly within bounds of a block */
    public void fixXY() {
        while (x % TILE_LENGTH != 0) x--;
        while (y % TILE_LENGTH != 0) y--;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
    }

    /**
     * Getter method, returns Player's x coordinate
     *
     * @return - an integer representation of the player's x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter method, returns Player's y coordinate
     *
     * @return - an integer representation of the player's y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Changes the Player's x or y coordinates depending on direction that Player is facing
     *
     * @param dir - the direction that Player is facing, set through constants FORWARD, RIGHT, BACKWARD, and LEFT
     */
    public void move(directions dir) {
        switch (dir) {
            case FORWARD:
                y += SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case BACKWARD:
                y -= SPEED;
                break;
            case LEFT:
                x -= SPEED;
                break;
        }
    }

    /**
     * Getter method, returns Player's texture
     *
     * @return - a Texture object representing Player's current texture
     */
    public Texture getTexture() {
        return textures[direction];
    }

    /**
     * Getter method, returns Player's direction
     *
     * @return - an integer representation of Player's direction, one of 4 values (0 through 3)
     */
    public int getDirectionID() {
        switch ( direc ) {
            case FORWARD:
                return 0;
            case RIGHT:
                return 1;
            case BACKWARD:
                return 2;
            case LEFT:
                return 3;
        }
        return -1;
    }

    public directions getDirection() {
        return direc;
    }

    public void updateDirection(){
        switch ( direc ) {
            case FORWARD:
                direction = 0;
                break;
            case RIGHT:
                direction = 1;
                break;
            case BACKWARD:
                direction = 2;
                break;
            case LEFT:
                direction = 3;
                break;
        }
    }

    /**
     * Getter method, returns Player's sprite     *
     *
     * @return - a Sprite object representing Player's current sprite
     */
    public Sprite getPlayerSprite() {
        updateDirection();
        return playerSprites[direction];
    }

    /* Records how long/how many consecutive times keys W, A, S, and D have been pressed */
    private void numPressHandler() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            numWKeyPressed++;
        } else {
            numWKeyPressed = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            numDKeyPressed++;
        } else {
            numDKeyPressed = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            numAKeyPressed++;
        } else {
            numAKeyPressed = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            numSKeyPressed++;
        } else {
            numSKeyPressed = 0;
        }

    }

    /**
     * Checks to see if player can move depending on direction they're facing and collisions in the direction
     *
     * @param dir - direction that player is facing
     * @return
     */
    private boolean canMove(int dir) {

        if (menuHandler.getState() != MenuHandler.states.FREE) return false;

        int playerX = x / TILE_LENGTH;
        int playerY = y / TILE_LENGTH;

        //checks if the block to which Player is attempting to move is valid (i.e. within bounds)

        int newX = 0;
        int newY = 0;

        if (direc == directions.FORWARD) {
            newX = playerX;
            newY = playerY + 1;
        } else if (direc == directions.BACKWARD) {
            newX = playerX;
            newY = playerY - 1;
        } else if (direc == directions.LEFT) {
            newX = playerX - 1;
            newY = playerY;
        } else if (direc == directions.RIGHT) {
            newX = playerX + 1;
            newY = playerY;
        }

        if (world.occupied(newX, newY)) return false;

        String tileName = world.getCurrentMap().getTileName(newX, newY);
        String tilePassable = world.getCurrentMap().getPassable(newX, newY);

        if (tileName != null && tileName.equals("black")) return false;

        if (world.getCurrentMap().getPassable(newX, newY) != null) return false;
        int doorID = world.getCurrentMap().getDoorID(newX, newY);

        if (doorID >= 0) {
            System.err.println("Changing map to " + doorID);
            Point p = world.changeMap(doorID);
            System.err.println("Changing player x and y to " + p.x + ", " + p.y);
            x = p.x * TILE_LENGTH;
            y = p.y * TILE_LENGTH;
        }


        return true;
    }
    public void changeMapSetCoords(int doorID){
        Point p = world.changeMap(doorID);
        x = p.x * TILE_LENGTH;
        y = p.y * TILE_LENGTH;
    }
    public void changeMapSetCoords(int doorID, int x, int y){
        world.changeMap(doorID);
        this.x = x;
        this.y = y;
        System.err.println("~~~~Reverting x,y to " + x + ", " + y);
    }

    /**
     * Checks to see if player can move depending on direction they're facing and collisions in the direction
     *
     * @return - true if player can move into tile, false otherwise
     */
    private boolean canMove() {
        int dir = direction;

        // cannot move when player is not free
        if (menuHandler.getState() != MenuHandler.states.FREE) return false;

        int playerX = x / TILE_LENGTH;
        int playerY = y / TILE_LENGTH;

        //checks if the block to which Player is attempting to move is valid (i.e. within bounds)

        int newX = 0;
        int newY = 0;

        if (direc == directions.FORWARD) {
            newX = playerX;
            newY = playerY + 1;
        } else if (direc == directions.BACKWARD) {
            newX = playerX;
            newY = playerY - 1;
        } else if (direc == directions.LEFT) {
            newX = playerX - 1;
            newY = playerY;
        } else if (direc == directions.RIGHT) {
            newX = playerX + 1;
            newY = playerY;
        }

        // check to see if NPC is on that tile
        if (world.occupied(newX, newY)) return false;

        // if tileName is "black" then movement is not allowed onto it
        String tileName = world.getCurrentMap().getTileName(newX, newY);
        if (tileName != null && tileName.equals("black")) return false;

        // all objects have a "Passable" property; not allowed movement onto objects
        if (world.getCurrentMap().getPassable(newX, newY) != null) return false;
        int doorID = world.getCurrentMap().getDoorID(newX, newY);

        // directs map change to world
        if (doorID >= 0) {
            System.err.println("Changing map to " + doorID);
            Point p = world.changeMap(doorID);
            System.err.println("Changing player x and y to " + p.x + ", " + p.y);
            x = p.x * TILE_LENGTH;
            y = p.y * TILE_LENGTH;
        }

        return true;
    }

    /* Handles player movement by checking keys pressed, how long they are pressed, and the direction of the player
     * Also handles playerSprite's x & y values
     * Also handles keyboard input of space and F
     */
    public void moveHandler() {

        // updates x and y values for all sprites
        for (int i = 0; i < 4; i++) {
            playerSprites[i].setX(x);
            playerSprites[i].setY(y);
        }

        numPressHandler();

        if (!isMoving()) {

            // handle SPACE key
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

                if (numSPACEKeyPressed == 0) {

                    // look for what tile player is facing
                    NPC npc = world.checkSpot(x / TILE_LENGTH, y / TILE_LENGTH, direction);

                    menuHandler.pressedSpace(npc);
                    numSPACEKeyPressed = 1;
                }
            } else numSPACEKeyPressed = 0;

            // handle F key
            if (Gdx.input.isKeyPressed(Input.Keys.F)) {

                if (numFKeyPressed == 0) {

                    menuHandler.pressedF();
                    numFKeyPressed = 1;

                }
            } else numFKeyPressed = 0;

            // handle W key
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {

                if (direc == directions.FORWARD && numWKeyPressed > KEY_SENSITIVITY) {
                    if (canMove())
                        moveCounterUp = TILE_LENGTH / 4;
                } else {
                    if (menuHandler.getState() == MenuHandler.states.FREE) {
                        changeDirection(directions.FORWARD);
                    }
                    moveCounterUp = 0;
                }
                return;
            }
            if (Gdx.input.isKeyPressed((Input.Keys.S))) {

                if (direc == directions.BACKWARD && numSKeyPressed > KEY_SENSITIVITY) {

                    if (canMove())
                        moveCounterDown = TILE_LENGTH / 4;
                } else {
                    if (menuHandler.getState() == MenuHandler.states.FREE) {
                        changeDirection(directions.BACKWARD);
                    }
                    moveCounterDown = 0;
                }
                return;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {

                if (direc == directions.LEFT && numAKeyPressed > KEY_SENSITIVITY) {

                    if (canMove())
                        moveCounterLeft = TILE_LENGTH / 4;
                } else {
                    if (menuHandler.getState() == MenuHandler.states.FREE) {
                        changeDirection(directions.LEFT);
                    }
                    moveCounterLeft = 0;
                }
                return;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {

                if (direc == directions.RIGHT && numDKeyPressed > KEY_SENSITIVITY) {

                    if (canMove())
                        moveCounterRight = TILE_LENGTH / 4;
                } else {
                    if (menuHandler.getState() == MenuHandler.states.FREE) {
                        changeDirection(directions.RIGHT);
                    }
                    moveCounterRight = 0;
                }
                return;
            }
        }

        if (moveCounterUp != 0) {
            moveCounterUp--;
            move(directions.FORWARD);
            isMoving = true;
        }
        if (moveCounterDown != 0) {
            moveCounterDown--;
            move(directions.BACKWARD);
            isMoving = true;
        }
        if (moveCounterLeft != 0) {
            moveCounterLeft--;
            move(directions.LEFT);
            isMoving = true;
        }
        if (moveCounterRight != 0) {
            moveCounterRight--;
            move(directions.RIGHT);
            isMoving = true;
        }

    }

    /**
     * Changes direction of the player
     *
     * @param dir - the direction to which the player is attempting to change (an integer value of 4 possibilities 0 through 3)
     */
    public void changeDirection(directions dir) {
        direc = dir;
    }

    /**
     * Determines whether or not the player is moving based off of the moveCounters (all must be 0 to be considered stationary, else moving)
     *
     * @return - true if player is moving, false otherwise
     */
    private boolean isMoving() {
        return isMoving = (moveCounterUp != 0 || moveCounterDown != 0 || moveCounterLeft != 0 || moveCounterRight != 0);
    }
    public void setSpellParser(SpellParser sp){
        if ( sp == null ) System.err.println("SPELLPARSER is NULL");
        if ( journal == null ) System.err.println("journal is NULL");
        journal.setSpellParser(sp);
    }

    // TODO: Add search/list of objects referenced with ID
    public boolean getItem(int objectID){
        if ( objectID == 0 ) {
            bag.addItem(Bag.type.BERRY, Bag.identifier.RED);
            return true;
        }
        return false;
    }
    public int getMapX(){
        return x / TILE_LENGTH;
    }
    public int getMapY(){
        return y / TILE_LENGTH;
    }

}
class Journal{
    private ArrayList<Spell> spells;
    private SpellParser spellParser;

    public Journal(){
        spells = new ArrayList<Spell>();
    }
    public void setSpellParser(SpellParser sp){
        spellParser = sp;
    }

    public void addSpell(int spellID){

        spells.add(spellParser.getSpell(spellID));
    }
    public void removeSpell(int spellID){
        spells.remove(spellParser.getSpell(spellID));
    }
    public ArrayList<Spell> getSpells(){
        return spells;
    }
}