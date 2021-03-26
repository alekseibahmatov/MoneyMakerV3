package activities.WineOfZamorak;

import activities.setup.Task;
import org.osbot.Sk;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.*;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;


public class TrainingMagic extends Task {

    private int selectedSpell = -1;

    @Override
    public boolean isNeededToStartAtGE() {
        return false;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);

        return (getInventory().contains("Mind rune") && getInventory().getItem("Mind rune").getAmount() > 10) &&
                (getInventory().contains("Water rune") && getInventory().getItem("Water rune").getAmount() > 10) &&
                (getInventory().contains("Earth rune") && getInventory().getItem("Earth rune").getAmount() > 10) &&
                (getInventory().contains("Fire rune") && getInventory().getItem("Fire rune").getAmount() > 10) &&
                (getInventory().contains("Chaos rune") && getInventory().getItem("Chaos rune").getAmount() > 10) &&
                getInventory().contains("Tuna") &&
                ((getEquipment().contains("Wooden shield") && getEquipment().contains("Staff of air")) ||
                        (getInventory().contains("Wooden shield") && getInventory().contains("Staff of air")));
    }

    private Area cows = new Area(3197, 3285, 3210, 3294);

    @Override
    public void execute(int type) {
        if (getBank().isOpen()) getBank().close();
        else if (getGrandExchange().isOpen()) getGrandExchange().close();

        putOnEquipment();

        if (getInventory().contains("Cowhide")) getInventory().getItem("Cowhide").interact("Drop");

        targetCow();

        if (!getCombat().isAutoRetaliateOn()) getCombat().toggleAutoRetaliate(true);

        analyzingMagicLvl();

        if (getSkills().getDynamic(Skill.HITPOINTS) > 6) {
            if (!getCombat().isFighting()) {
                attackCow();
            }
        } else heal();

        if (!cows.contains(myPosition())) {
            log("casted spell = -1");
            selectedSpell = -1;
        }
    }


    private void putOnEquipment() {
        if (!getEquipment().contains("Wooden shield") && getInventory().contains("Wooden shield"))
            getInventory().getItem("Wooden shield").interact("Wield");

        Sleep.sleepUntil(random(500, 750));

        if (!getEquipment().contains("Staff of air") && getInventory().contains("Staff of air"))
            getInventory().getItem("Staff of air").interact("Wield");

        Sleep.sleepUntil(random(500, 750));
    }

    private void targetCow() {
        while (!cows.contains(myPosition())) getWalking().webWalk(cows);

        new ConditionalSleep(60000, 1000) {
            @Override
            public boolean condition() {
                NPC cow = getNpcs().closest("Cow");
                getCamera().toEntity(cow);
                return (cow != null && cow.isVisible());
            }
        }.sleep();
        log("cow targeted");
    }

    public void openSpellMenu() {
        RS2Widget combatTab = getWidgets().get(548, 58);
        combatTab.interact();
        Sleep.sleepUntil(random(1250, 1500));
        RS2Widget spellBtn = getWidgets().get(593, 21, 4);
        spellBtn.interact();
        Sleep.sleepUntil(random(1250, 1500));
    }


    private void attackCow() {

        if (selectedSpell != -1) {

            NPC cow = getNpcs().closest("Cow");

            if (cow != null && cow.isVisible()) {

                getCamera().toEntity(cow);

                Sleep.sleepUntil(random(300, 500));

                if (!getCombat().isFighting() || myPlayer().getInteracting() == null) {
                    if (cow.isVisible()) {
                        cow.interact("Attack");
                    }
                }

                new ConditionalSleep(20000, 5000) {
                    @Override
                    public boolean condition() throws InterruptedException {
                        log("fight sleep");
                        return (!getCombat().isFighting() || getSkills().getDynamic(Skill.HITPOINTS) < 6);
                    }
                }.sleep();
            }

        }
    }

    private void analyzingMagicLvl() {
        NPC cow = getNpcs().closest("Cow");

        log("casted spell:" + selectedSpell);

        if (getSkills().getDynamic(Skill.MAGIC) >= 29 && selectedSpell != 6) {
            openSpellMenu();
            RS2Widget lvl29Spell = getWidgets().get(201, 1, 7);
            lvl29Spell.interact();

            Sleep.sleepUntil(random(500, 750));
            getMagic().castSpellOnEntity(Spells.NormalSpells.EARTH_BOLT, cow);
            selectedSpell = 6;
        } else if (getSkills().getDynamic(Skill.MAGIC) >= 23 && getSkills().getDynamic(Skill.MAGIC) <= 28 && selectedSpell != 5) {
            openSpellMenu();
            RS2Widget lvl23Spell = getWidgets().get(201, 1, 6);
            lvl23Spell.interact();

            Sleep.sleepUntil(random(500, 750));
            getMagic().castSpellOnEntity(Spells.NormalSpells.WATER_BOLT, cow);
            selectedSpell = 5;
        } else if (getSkills().getDynamic(Skill.MAGIC) >= 17 && getSkills().getDynamic(Skill.MAGIC) <= 22 && selectedSpell != 4) {
            openSpellMenu();
            RS2Widget lvl17Spell = getWidgets().get(201, 1, 5);
            lvl17Spell.interact();

            Sleep.sleepUntil(random(500, 750));
            getMagic().castSpellOnEntity(Spells.NormalSpells.WIND_BOLT, cow);
            selectedSpell = 4;
        } else if (getSkills().getDynamic(Skill.MAGIC) >= 13 && getSkills().getDynamic(Skill.MAGIC) <= 16 && selectedSpell != 3) {
            openSpellMenu();
            RS2Widget lvl13Spell = getWidgets().get(201, 1, 4);
            lvl13Spell.interact();

            Sleep.sleepUntil(random(500, 750));
            getMagic().castSpellOnEntity(Spells.NormalSpells.FIRE_STRIKE, cow);
            selectedSpell = 3;
        } else if (getSkills().getDynamic(Skill.MAGIC) >= 9 && getSkills().getDynamic(Skill.MAGIC) <= 12 && selectedSpell != 2) {
            openSpellMenu();
            RS2Widget lvl9Spell = getWidgets().get(201, 1, 3);
            lvl9Spell.interact();

            Sleep.sleepUntil(random(500, 750));
            getMagic().castSpellOnEntity(Spells.NormalSpells.EARTH_STRIKE, cow);
            selectedSpell = 2;
        } else if (getSkills().getDynamic(Skill.MAGIC) >= 5 && getSkills().getDynamic(Skill.MAGIC) <= 8 && selectedSpell != 1) {
            openSpellMenu();
            RS2Widget lvl5Spell = getWidgets().get(201, 1, 2);
            lvl5Spell.interact();

            Sleep.sleepUntil(random(500, 750));
            getMagic().castSpellOnEntity(Spells.NormalSpells.WATER_STRIKE, cow);
            selectedSpell = 1;
        } else if (getSkills().getDynamic(Skill.MAGIC) >= 1 && getSkills().getDynamic(Skill.MAGIC) <= 4 && selectedSpell != 0) {
            openSpellMenu();
            RS2Widget lvl1Spell = getWidgets().get(201, 1, 1);
            lvl1Spell.interact();

            Sleep.sleepUntil(random(500, 750));
            getMagic().castSpellOnEntity(Spells.NormalSpells.WIND_STRIKE, cow);
            selectedSpell = 0;
        }

        log("spell manipulations");
    }

    private void heal() {
        log("healing");

        if (getInventory().contains("Tuna")) {
            while (getSkills().getDynamic(Skill.HITPOINTS) < 9) {
                getInventory().getItem("Tuna").interact("Eat");
            }
        }

    }

//    if (getMagic().canCast(Spells.NormalSpells.WIND_STRIKE)) {
//        if (getMagic().canCast(Spells.NormalSpells.WATER_STRIKE)) {
//            if (getMagic().canCast(Spells.NormalSpells.EARTH_STRIKE)) {
//                if (getMagic().canCast(Spells.NormalSpells.FIRE_STRIKE)) {
//                    if (getMagic().canCast(Spells.NormalSpells.WIND_BOLT)) {
//                        if (getMagic().canCast(Spells.NormalSpells.WATER_BOLT)) {
//                            if (getMagic().canCast(Spells.NormalSpells.EARTH_BOLT)) {
//                                analyzingMagicLvl();
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

}


