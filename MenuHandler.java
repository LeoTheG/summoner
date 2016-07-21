package gharib.leonar.summoner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
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
        FREE, CHATTING, INCHAT, MAINMENU
    }

    private states s;
    private ArrayList<Sprite> sprites;

    private static int TILE_LENGTH = 64;
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 1280;

    private String currentChat = "";

    private Sprite textBox = new Sprite(new Texture("UI/gray-box.png"));
    private Sprite mainMenuBox = new Sprite(new Texture("UI/menu-box.png"));
    private Sprite cursor = new Sprite(new Texture("UI/cursor.png"));

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

    private boolean newChat;

    /** Constructor which begins states as FREE */
    public MenuHandler() {
        s = states.FREE;
        sprites = new ArrayList<Sprite>();

        // properly scale menu items
        textBox.setScale(6.5f, 3.5f);
        mainMenuBox.setScale(3.5f, 2f);
        cursor.setScale(0.2f, 0.2f);

        cursorPoint = new Point(0, 0);
        cursorSelection = 0;

        newChat = true;
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

    /**
     * Change menu when SPACE is pressed depending on menu state
     */
    public void pressedSpace(NPC npc) {

        if (npc == null)
            setChat("");

        if (s == states.FREE) {

            setChat(npc.getChat(true));
            if (currentChat.length() == 0) return;
            sprites.add(textBox);
            s = states.CHATTING;

        }
        else if (s == states.CHATTING) {

            s = states.INCHAT;

        }
        else if (s == states.INCHAT) {

            if ( npc.finishedChat() ) {
                sprites.remove(textBox);
                s = states.FREE;
            }
            else{
                setChat(npc.getChat(true));
                s = states.CHATTING;
                newChat = true;
            }
            /*
            sprites.remove(textBox);
            s = states.FREE;
            */
        }
        else if (s == states.MAINMENU) {

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
        currentChat = str;
    }

    /**
     * Handles menu and cursor depending on keyboard input
     */
    public void menuHandle() {

        if (s != states.MAINMENU) return;

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

        // update cursorPoint
        cursorPoint.x = player.getX() + CURSOR_OFFSET_X;
        cursorPoint.y = player.getY() + CURSOR_OFFSET_Y - (CURSOR_SELEC_Y * cursorSelection);
    }
}
