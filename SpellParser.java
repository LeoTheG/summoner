package gharib.leonar.summoner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.util.ArrayList;

/* Author       : Leonar Gharib
   Date Created : 8/18/2016
   Purpose      : Read from spell file into game
*/
public class SpellParser {

   private static final String ID = "<ID>";
   private static final String NAME = "<Name>";
   private static final String DAMAGE = "<Damage>";
   private static final String ENERGYCOST = "<EnergyCost>";
   private static final String MAGICCOST = "<MagicCost>";

   private static BufferedReader in;

   // number of lines per spell that describes its properties
   private static final int NUM_PROP_LINE = 6;

   // list of all spells
   private ArrayList<Spell> spells = new ArrayList<Spell>();

   public SpellParser(){

      try {
         FileHandle file = Gdx.files.internal("data/Spells/Spells.txt");
         in = new BufferedReader(file.reader());
      }
      catch(Exception e){System.err.println(e.getMessage());}
      parse();

   }

   public Spell getSpell(int spellID){
      return spells.get(spellID);
   }
   private void parse(){
      // read line-by-line
      String line;

      int i = 0;

      int numTotalSpells = 0;

      int spellID = 0;
      String spellName = "";
      int spellDamage = 0;
      int energyCost = 0;
      int magicCost = 0;


      try{
         line = in.readLine();

         // parse 1st line -- # total NPCS
         numTotalSpells = Integer.parseInt(line);
         //System.err.println("Num total NPCS: "+ numTotalNPCs);
      }
      catch (Exception e) {System.err.println("ERRORAF: " + e.getMessage()); }

      int pos = 0;

      while (i < (NUM_PROP_LINE * numTotalSpells + 1)) {

         int index = i % NUM_PROP_LINE;

         //System.err.println("~~index = " + index);

         try {

            line = in.readLine();

            //System.err.println("Read line: " + line);

            switch (index) {
               case 0:
                  //System.err.println("case 0 : skip");
                  break;
               case 1:
                  pos = line.indexOf(ID) + ID.length();
                  spellID = Integer.parseInt(line.substring(pos));
                  //System.err.println("spellID: " + spellID);
                  break;
               case 2:
                  pos = line.indexOf(NAME) + NAME.length();
                  spellName = line.substring(pos);
                  //System.err.println("spell name: " + spellName);
                  break;
               case 3:
                  pos = line.indexOf(DAMAGE) + DAMAGE.length();
                  spellDamage = Integer.parseInt(line.substring(pos));
                  //System.err.println("spell damage: " + spellDamage);
                  break;
               case 4:
                  pos = line.indexOf(ENERGYCOST) + ENERGYCOST.length();
                  energyCost = Integer.parseInt(line.substring(pos));
                  //System.err.println("energy cost: " + energyCost);
                  break;
               case 5:
                  pos = line.indexOf(MAGICCOST) + MAGICCOST.length();
                  magicCost = Integer.parseInt(line.substring(pos));
                  //System.err.println("magic cost: " + magicCost);
                  break;
            }
            // after last line for Spell, create Spell object and add to spells
            if (index == NUM_PROP_LINE - 1) {

               Spell spell = new Spell(spellName,spellDamage,magicCost,energyCost,spellID);

               spells.add(spell);
               //System.err.println("Added spell with ID: " + spellID);

            }
         }
         catch (Exception e2) {
            System.err.println("ERROR : " + e2.getMessage());
            e2.printStackTrace();
         }

         i++;

      }


   }

}
