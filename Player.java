package gharib.leonar.summoner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.File;

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
    //player's (x, y) coordinates
    private int x;
    private int y;
    //direction player is facing (values are constants FORWARD, RIGHT, BACKWARD, LEFT)
    private int direction;
    //integers that measure whether or not the player is moving in the direction (0 if not moving, max value = TILE_LENGTH/2)
    private int moveCounterUp = 0, moveCounterDown = 0, moveCounterLeft = 0, moveCounterRight = 0;
    //used in conjunction with KEY_SENSITIVITY to tell how long the respective movement keys have been pressed
    private int numWKeyPressed, numDKeyPressed, numSKeyPressed, numAKeyPressed,
                numSPACEKeyPressed;
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
    /** Constructor for Player, sets up initial variables
     * @param p - the Preferences object of BoxmonGame (used for loading information about Player
     */
    public Player(Preferences p, World w, MenuHandler m)
    {
        world = w;
        prefs = p;
        menuHandler = m;
        x = prefs.getInteger("playerX", 0);
        y = prefs.getInteger("playerY", 0);
        direction = prefs.getInteger("playerDirection");
        init();
    }
    /** Second constructor for Player, instead of Preferences, x and y variables are assigned through parameters
     * NOTE: CURRENTLY NOT IN USE
     * @param x - the x coordinate of Player
     * @param y - the y coordinate of Player
     */
    public Player(int x, int y)
    {
        this.x = x;
        this.y = y;
        init();
    }
    /* Initializes certain variables, called in constructor */
    private void init()
    {
        textures = new Texture[4];
        textures[0] = new Texture(new FileHandle(new File("characters\\PlayerTexture.png")));
        textures[2] = new Texture(new FileHandle(new File("characters\\PlayerTextureBack.png")));
        textures[3] = new Texture(new FileHandle(new File("characters\\PlayerTextureLeft.png")));
        textures[1] = new Texture(new FileHandle(new File("characters\\PlayerTextureRight.png")));

        isMoving = false;
        //direction = FORWARD;
        playerSprites = new Sprite[4];
        playerSprites[FORWARD] = new Sprite(textures[BACKWARD]);
        playerSprites[BACKWARD] = new Sprite(textures[FORWARD]);
        playerSprites[LEFT] = new Sprite(textures[LEFT]);
        playerSprites[RIGHT] = new Sprite(textures[RIGHT]);

    }
    /* Fixes the x & y coordinate of Player if Player is not exactly within bounds of a block */
    public void fixXY()
    {
        while(x%TILE_LENGTH != 0) x--;
        while(y%TILE_LENGTH != 0) y--;
        if(x < 0) x = 0;
        if(y < 0) y = 0;
    }
    /** Getter method, returns Player's x coordinate
     * @return - an integer representation of the player's x coordinate
     */
    public int getX()
    {
        return x;
    }
    /** Getter method, returns Player's y coordinate
     * @return - an integer representation of the player's y coordinate
     */
    public int getY()
    {
        return y;
    }
    /** Changes the Player's x or y coordinates depending on direction that Player is facing
     * @param dir - the direction that Player is facing, set through constants FORWARD, RIGHT, BACKWARD, and LEFT
     */
    public void move(int dir)
    {
        switch(dir)
        {
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
    /** Getter method, returns Player's texture
     * @return - a Texture object representing Player's current texture
     */
    public Texture getTexture()
    {
        return textures[direction];
    }

    /** Getter method, returns Player's direction
     * @return - an integer representation of Player's direction, one of 4 values (0 through 3)
     */
    public int getDirection()
    {
        return direction;
    }

    /** Getter method, returns Player's sprite     *
     * @return - a Sprite object representing Player's current sprite
     */
    public Sprite getPlayerSprite()
    {
        //if ( direction == BACKWARD ) return playerSprites[FORWARD];
        //if ( direction == FORWARD ) return playerSprites[BACKWARD];
        return playerSprites[direction];
    }
    /* Records how long/how many consecutive times keys W, A, S, and D have been pressed */
    private void numPressHandler()
    {
        if(Gdx.input.isKeyPressed(Input.Keys.W))
        {
            numWKeyPressed++;
        }
        else
        {
            numWKeyPressed = 0;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D))
        {
            numDKeyPressed++;
        }
        else
        {
            numDKeyPressed = 0;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A))
        {
            numAKeyPressed++;
        }
        else
        {
            numAKeyPressed = 0;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S))
        {
            numSKeyPressed++;
        }
        else
        {
            numSKeyPressed = 0;
        }

    }
    //TODO: after NPCs are added, check for NPC collision
    private boolean canMove(int dir)
    {

        int maxWidth  = world.getCurrentMap().getDimensions()[0];
        int maxHeight = world.getCurrentMap().getDimensions()[1];
        int playerX   = x/TILE_LENGTH;
        int playerY   = y/TILE_LENGTH;


        if ( menuHandler.getState() == MenuHandler.states.INCHAT ){
            return false;
        }

        //checks if the block to which Player is attempting to move is valid (i.e. within bounds)
        if(dir == FORWARD)
        {
            if ( world.occupied(new Point(playerX, playerY+1))) return false;
            return playerY + 1 < maxHeight;
        }
        if(dir == BACKWARD)
        {
            if ( world.occupied(new Point(playerX, playerY-1))) return false;
            return playerY - 1 >= 0;
        }
        if(dir == RIGHT)
        {
            if ( world.occupied(new Point(playerX+1, playerY))) return false;
            return playerX + 1 < maxWidth;
        }
        if(dir == LEFT)
        {
            if ( world.occupied(new Point(playerX-1, playerY))) return false;
            return playerX -1 >= 0;
        }


        return false;
    }
    /* Handles player movement by checking keys pressed, how long they are pressed, and the direction of the player
     * Also handles rotation of player and playerSprite's x & y values
     */
    public void moveHandler()
    {
        for ( int i = 0; i < 4; i++ ) {
            playerSprites[i].setX(x);
            playerSprites[i].setY(y);
        }

        numPressHandler();

        if(!isMoving()) {

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                if ( numSPACEKeyPressed == 0 ) {
                    // look for what tile player is facing
                    NPC npc = world.checkSpot(x/TILE_LENGTH,y/TILE_LENGTH,direction);
                    if ( npc != null ){
                        menuHandler.setChat(npc.getChat());
                    }
                    else
                        menuHandler.setChat("");
                    menuHandler.pressedSpace();
                    numSPACEKeyPressed = 1;
                    System.out.println("Pressed space");

                }
            }
            else numSPACEKeyPressed = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {

                if(direction == FORWARD && numWKeyPressed > KEY_SENSITIVITY) {
                    if(canMove(FORWARD))
                        moveCounterUp = TILE_LENGTH/4;
                }

                else {
                    if (menuHandler.getState() == MenuHandler.states.FREE) {
                        changeDirection(FORWARD);
                    }
                    moveCounterUp = 0;
                }
                return;
            }
            if (Gdx.input.isKeyPressed((Input.Keys.S))) {

                if(direction == BACKWARD && numSKeyPressed > KEY_SENSITIVITY) {
                    if(canMove(BACKWARD))
                        moveCounterDown = TILE_LENGTH/4;
                }
                else {
                    if (menuHandler.getState() == MenuHandler.states.FREE) {
                        changeDirection(BACKWARD);
                    }
                    moveCounterDown = 0;
                }
                return;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {

                if(direction == LEFT && numAKeyPressed > KEY_SENSITIVITY) {
                    if(canMove(LEFT))
                        moveCounterLeft = TILE_LENGTH/4;
                }
                else {
                    if (menuHandler.getState() == MenuHandler.states.FREE) {
                        changeDirection(LEFT);
                    }
                    moveCounterLeft = 0;
                }
                return;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {

                if(direction == RIGHT && numDKeyPressed > KEY_SENSITIVITY) {
                    if(canMove(RIGHT))
                        moveCounterRight = TILE_LENGTH/4;
                }
                else {
                    if (menuHandler.getState() == MenuHandler.states.FREE) {
                        changeDirection(RIGHT);
                    }
                    moveCounterRight = 0;
                }
                return;
            }
        }
        if(moveCounterUp != 0)
        {
            moveCounterUp--;
            move(FORWARD);
            isMoving = true;
        }
        if(moveCounterDown != 0)
        {
            moveCounterDown--;
            move(BACKWARD);
            isMoving = true;
        }
        if(moveCounterLeft != 0)
        {
            moveCounterLeft--;
            move(LEFT);
            isMoving = true;
        }
        if(moveCounterRight != 0)
        {
            moveCounterRight--;
            move(RIGHT);
            isMoving = true;
        }
    }
    /** Changes direction of the player
     * @param dir - the direction to which the player is attempting to change (an integer value of 4 possibilities 0 through 3)
     */
    public void changeDirection(int dir)
    {
        /*
        if(direction == FORWARD)
        {
            playerSprite = new Sprite(textures[2]);
            /*
            switch(dir)
            {
                case FORWARD:
                    break;
                case RIGHT:
                    //playerSprite.rotate(270);
                    playerSprite.rotate(270);
                    break;
                case BACKWARD:
                    playerSprite.rotate(180);
                    break;
                case LEFT:
                    playerSprite.rotate(90);
                    break;
            }


        }
        else if(direction == RIGHT)
        {
            playerSprite = new Sprite(textures[1]);
            /*
            switch(dir)
            {
                case FORWARD:
                    playerSprite.rotate(90);
                    break;
                case RIGHT:
                    break;
                case BACKWARD:
                    playerSprite.rotate(270);
                    break;
                case LEFT:
                    playerSprite.rotate(180);
                    break;
            }

        }
        else if(direction == BACKWARD)
        {
            playerSprite = new Sprite(textures[0]);
            /*
            switch(dir)
            {
                case FORWARD:
                    playerSprite.rotate(180);
                    break;
                case RIGHT:
                    playerSprite.rotate(90);
                    break;
                case BACKWARD:
                    break;
                case LEFT:
                    playerSprite.rotate(270);
                    break;
            }

        }
        else if(direction == LEFT)
        {
            playerSprite = new Sprite(textures[3]);
            /*
            switch(dir)
            {
                case FORWARD:
                    playerSprite.rotate(270);
                    break;
                case RIGHT:
                    playerSprite.rotate(180);
                    break;
                case BACKWARD:
                    playerSprite.rotate(90);
                    break;
                case LEFT:
                    break;
            }

        }
        */

        direction = dir;
    }
    /* Determines whether or not the player is moving based off of the moveCounters (all must be 0 to be considered stationary, else moving) */
    private boolean isMoving()
    {
        return isMoving = (moveCounterUp != 0 || moveCounterDown != 0 || moveCounterLeft != 0 || moveCounterRight != 0);
    }
}