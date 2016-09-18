package gharib.leonar.summoner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

/* Author       : Leonar Gharib
   Date Created : 7/20/2016
   Purpose      : Used to resolve battles.
*/
public class BattleHandler {

   private static final int TILE_LENGTH = 64;

   private double startTime;
   private Spirit playerSp;
   private Spirit enemySp;

   private ArrayList<Spell> playerSpells = new ArrayList<Spell>();

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

   private boolean flagEndBattle;

   private NPC enemyNPC;

   public enum battleStates {
      FREE, BEG, BEG_COMP, MAIN, SPELL, PACK, END
   }

   private boolean setBarXY;

   private battleStates bs;

   private static final double tickSpeed = 0.8;

   private Player player;

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

      enemyNPC = null;

   }
   public void setPlayer(Player p){
      player = p;
      playerSpells = player.getSpells();
   }

   public boolean run(){
      System.err.println("Run successful");
      setBarXY = false;
      return true;
   }
   public NPC getEnemyNPC(){
      return enemyNPC;
   }

   public Sprite[] getSprites(){

      System.err.println("Getting sprites");

      Sprite[] arr = new Sprite[2];

      arr[0] = playerSp.getSprite();


      if ( !enemySp.getFlopped() && bs == battleStates.END) {
         System.err.println("Flopping enemy");
         enemySp.flop(true);
      }
      else
         System.err.println("Not flopping enemy");

      arr[1] = enemySp.getSprite();

      return arr;
   }

   public void renderBars(SpriteBatch batch){

      float lifePerc = (float)enemySp.getHP() / (float)enemySp.getMaxHP();
      float energyPerc = (float) playerSp.getEnergy() / (float) playerSp.getMaxEnergy();
      float manaPerc = (float) playerSp.getMana() / (float) playerSp.getMaxMana();

      for ( Sprite s : bars ) {
         if ( s == bars[1] )
            s.setScale(manaPerc, 1);
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

   public boolean playerCanCast(int spellIndex){

      Spell playerSpell = playerSpells.get(spellIndex);

      int energyCost = playerSpell.getEnergyCost();
      int magicCost = playerSpell.getMagicCost();

      int playerEnergy = playerSp.getEnergy();
      int playerMana = playerSp.getMana();

      if ( playerEnergy >= energyCost && playerMana >= magicCost ) return true;
      else return false;

   }

   public void handle(){

   }

   public boolean castSpell(int selection){
      if ( playerCanCast(selection) ){

         Spell spell = playerSpells.get(selection);

         int spellDamage = spell.getDamage();
         int enemyDef = enemySp.getDefense();

         // calculate spell damage
         int damageInflicted = spellDamage - enemyDef;

         enemySp.takeDamage(damageInflicted);
         if ( enemySp.getHP() < 0 ) enemySp.setHP(0);

         playerSp.loseMana(spell.getMagicCost());
         playerSp.loseEnergy(spell.getEnergyCost());

         if ( enemySp.getHP() <= 0 ) {
            System.err.println("Flagging end battle");
            flagEndBattle = true;
         }
         return true;
      }
      else return false;
   }

   public String[] getMainText(){

      if ( flagEndBattle ) {
         String[] strArr = {"Defeated enemy!"};
         return strArr;
      }

      return options;
   }

   public void resetTimer(){
      startTime = TimeUtils.millis();
   }

   public void attack(boolean onPlayer){
      if ( onPlayer ) playerSp.attackedBy(enemySp);
      else {
         if ( playerSp.getEnergy() < playerSp.getAtkCost() ) return;
         playerSp.loseEnergy(playerSp.getAtkCost());
         System.err.println("Energy now at : " + playerSp.getEnergy());
         enemySp.takeDamage(playerSp.getAttackDamage() - enemySp.getDefense());

         if ( enemySp.getHP() < 0 ) enemySp.setHP(0);

         if ( enemySp.getHP() <= 0 ) {
            System.err.println("Flagging end battle");
            flagEndBattle = true;
            enemySp.flop(true);
         }
      }
   }
   public String[] getSpellText(){

      String[] spellTexts = new String[playerSpells.size()];

      for (int i = 0; i < playerSpells.size(); i++) {
         spellTexts[i] = playerSpells.get(i).getName();
      }

      return spellTexts;
   }

   public boolean checkTimer(){

      /*
      if ( enemySp.getHP() == 0  ) {
         System.err.println("Flagging end battle");
         flagEndBattle = true;
      }
      */

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

   public void begin(Spirit playerSp, Spirit enemySp, NPC enemyNPC) {

      //playerSpells = playerSp.getSpells();

      this.enemyNPC = enemyNPC;

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
      if ( enemySp.getFlopped() == true ) {
         enemySp.unFlop(true);
      }


   }

   public void tick() {

      // if enough time has passed, then tick
      if ( checkTimer() ) {
         playerSp.regenEnergy();
         enemySp.regenEnergy();

         playerSp.regenMana();
         enemySp.regenMana();



      }



   }

}
