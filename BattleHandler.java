package gharib.leonar.summoner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;

/* Author       : Leonar Gharib
   Date Created : 7/20/2016
   Purpose      : Used to resolve battles.
*/
public class BattleHandler {

   private static final int TILE_LENGTH = 64;

   private double startTime;
   private Spirit playerSp;
   private Spirit enemySp;

   private boolean pressedSpace;
   private boolean canBegin;
   private boolean inBattle;

   private String[] options;

   private Sprite[] bars;

   // enemy sprite and player sprite offsets
   private static int PLAYER_BARS_XOFFSET = 136;
   private static int PLAYER_BARS_YOFFSET = 150;

   private static int ENEMY_BARS_XOFFSET = -200;
   private static int ENEMY_BARS_YOFFSET = 150;

   private Spell[] playerSpells;

   private boolean flagEndBattle;

   public enum battleStates {
      FREE, BEG, BEG_COMP, MAIN, SPELL, PACK
   }

   private boolean setBarXY;

   private battleStates bs;

   private static final double tickSpeed = 0.8;

   /** Constructor */
   public BattleHandler(){

      flagEndBattle = false;

      pressedSpace = false;
      canBegin = false;
      inBattle = false;

      bs = battleStates.FREE;

      options = new String[4];

      options[0] = "ATK";
      options[1] = "MAG";
      options[2] = "PACK";
      options[3] = "RUN";

      bars = new Sprite[4];

      // player life, mana, energy
      bars[0] = new Sprite(new Texture(Gdx.files.internal("data/UI/life.png")));
      bars[1] = new Sprite(new Texture(Gdx.files.internal("data/UI/mana.png")));
      bars[2] = new Sprite(new Texture(Gdx.files.internal("data/UI/energy.png")));
      // enemy life
      bars[3] = new Sprite(new Texture(Gdx.files.internal("data/UI/life.png")));

      setBarXY = false;

   }

   public boolean run(){
      System.err.println("Run successful");
      setBarXY = false;
      return true;
   }

   public Sprite[] getSprites(){

      Sprite[] arr = new Sprite[2];
      arr[0] = playerSp.getSprite();
      Sprite enemySprite = enemySp.getSprite();
      //enemySprite.flip(true,false);
      arr[1] = enemySprite;

      return arr;
   }

   public void renderBars(SpriteBatch batch){

      float lifePerc = (float)enemySp.getHP() / (float)enemySp.getMaxHP();
      float energyPerc = (float) playerSp.getEnergy() / (float) playerSp.getMaxEnergy();

      for ( Sprite s : bars ) {
         if ( s == bars[3] )
            s.setScale(lifePerc, 1);
         if ( s == bars[2] )
            s.setScale(energyPerc, 1);
         s.draw(batch);
      }
   }

   public void setFlagEndBattle(boolean b){
      flagEndBattle = b;
   }

   public boolean getFlagEndBattle(){
      return flagEndBattle;
   }

   public battleStates getBattleState(){
      return bs;
   }

   public void setCanBegin(boolean b){
      canBegin = b;
   }

   public boolean canBegin(){
      return canBegin;
   }

   public int getPlayerEnergy(){
      return playerSp.getEnergy();
   }

   public int getPlayerAtkCost(){
      return playerSp.getAtkCost();
   }

   public void handle(){

   }

   public void castSpell(int selection){

   }

   public String[] getMainText(){
      return options;
   }

   public void resetTimer(){
      startTime = TimeUtils.millis();
   }

   public void attack(boolean onPlayer){
      if ( onPlayer ) playerSp.attackedBy(enemySp);
      else {
         enemySp.attackedBy(playerSp);
      }
   }
   public String[] getSpellText(){
      String[] strings = {playerSp.getSpell().getName()};

      return strings;
   }

   public boolean checkTimer(){

      if ( enemySp.getHP() == 0 ) {
         flagEndBattle = true;
      }

      double elapsedTime = TimeUtils.timeSinceMillis((long)startTime);

      elapsedTime /= 1000;

      if ( elapsedTime >= tickSpeed ){
         resetTimer();
         return true;
      }
      return false;
   }

   public void setBattleState(battleStates b){
      bs = b;
   }

   public String getChat(){

      if ( bs == battleStates.BEG || bs == battleStates.BEG_COMP ) {
         return "Enemy wants to\nfight!";
      }
      else if ( bs == battleStates.MAIN )
         return "ATK\tMAG\nPACK\tRUN";
      else return "";
   }

   public void begin(Spirit playerSp, Spirit enemySp, int playerX, int playerY) {

      //playerSpells = playerSp.getSpells();

      if ( !setBarXY ) {
         setBarXY = true;
         for ( int i = 0; i < 3; i++ ) {
            bars[i].setX(10 * TILE_LENGTH + PLAYER_BARS_XOFFSET);
            bars[i].setY(10 * TILE_LENGTH + PLAYER_BARS_YOFFSET - (i*bars[i].getHeight()));
         }
         bars[3].setX(10 * TILE_LENGTH + ENEMY_BARS_XOFFSET);
         bars[3].setY(10 * TILE_LENGTH + ENEMY_BARS_YOFFSET);
      }

      inBattle = true;
      bs = battleStates.BEG;

      this.playerSp = playerSp;
      this.enemySp = enemySp;

      canBegin = false;
      playerSp.battleReset();
      enemySp.battleReset();

      if (enemySp.getFlipped() == false ) {
         enemySp.flip();
      }


   }

   public void tick() {

      // if enough time has passed, then tick
      if ( checkTimer() ) {
         playerSp.regenEnergy();
         enemySp.regenEnergy();



      }



   }

}
