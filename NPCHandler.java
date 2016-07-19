package gharib.leonar.summoner;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/* Author       : Leonar Gharib
   Date Created : 6/26/2016
   Purpose      : Handle rendering of NPCs.
*/
public class NPCHandler {

    private static int TILE_LENGTH = 64;

    private PriorityQueue<NPC> pq = new PriorityQueue<NPC>();

    private SpriteBatch batch;

    /** Constructor
     *
     * @param batch - batch used for rendering sprites.
     */
    public NPCHandler(SpriteBatch batch) {
        this.batch = batch;

    }

    /** Remove all NPCs from priority queue */
    public void clearNPCs() {
        pq.removeAll(pq);
    }

    /** Add various amounts of NPCs to queue */
    public void add(NPC[] npc) {

        for (int i = 0; i < npc.length; i++) {
            pq.add(npc[i]);
        }
    }

    /** Add single NPC to queue */
    public void add(NPC npc) {
        pq.add(npc);
    }

    /** Draw sprites and player in proper order.
     *
     * @param p - player object from which information about y value and sprite is extracted.
     */
    public void render(Player p) {

        // want to render highest Y value first to lowest Y value last

        NPC[] arr1 = new NPC[pq.size()];
        NPC[] arr = pq.toArray(arr1);
        int playerY = p.getY() / TILE_LENGTH;

        // i is first index where player Y has higher Y value than element (NPC)
        int i;
        for (i = 0; i < arr.length; i++) {
            if (playerY > arr[i].y) {
                break;
            }
        }

        // first draw all of the NPCs with a higher y value than player
        int j;
        for (j = 0; j <= i - 1; j++) {
            arr[j].getSprite().draw(batch);
        }

        // then draw player
        p.getPlayerSprite().draw(batch);

        // lastly draw all NPCs with a lower y value than player
        for (int k = j; k < arr.length; k++) {
            arr[k].getSprite().draw(batch);
        }

    }

}