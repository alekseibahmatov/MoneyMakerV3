package activities.WineOfZamorak;

import activities.setup.Task;
import org.osbot.rs07.api.Camera;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.ui.*;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Hopper;
import utils.Sleep;

import java.util.ArrayList;
import java.util.List;

public class WineOfZamorak extends Task {
    @Override
    public boolean isNeededToStartAtGE() {
        return false;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);

        return (getInventory().contains("Law rune") &&
                getInventory().getAmount("Law rune") > random(15, 20)) &&
                (getInventory().contains("Restore potion(4)") ||
                        getInventory().contains("Restore potion(3)") ||
                        getInventory().contains("Restore potion(2)") ||
                        getInventory().contains("Restore potion(1)")) &&
                (getEquipment().contains("Zamorak monk top") || getInventory().contains("Zamorak monk top")) &&
                (getEquipment().contains("Zamorak monk bottom") || getInventory().contains("Zamorak monk bottom")) &&
                (getEquipment().contains("Staff of air") || getInventory().contains("Staff of air")) &&
                getInventory().contains("Tuna");
    }

    private Area wineFirst = new Area(2940, 3517, 2940, 3517);

    private Area safeSpot = new Area(2942, 3517, 2943, 3517);

    private int world = -1;

    private int iteration = -1;

    private boolean spizdil = false;

    private boolean changedServer = false;

    private Hopper hopper = new Hopper();

    String[] potions = {"Restore potion(1)", "Restore potion(2)", "Restore potion(3)", "Restore potion(4)"};

//    @Override
//    public void execute(int type) {
//
//        world = getWorlds().getCurrentWorld();
//
//        iteration = -1;
//
//        if (getBank().isOpen()) getBank().close();
//        else if (getGrandExchange().isOpen()) getGrandExchange().close();
//
//        if (getInventory().contains("Vial")) getInventory().getItem("Vial").interact("Drop");
//        Sleep.sleepUntil(random(500, 750));
//
//        putOnEquipment();
//        log("checked equipment");
//
//        if (getCombat().isAutoRetaliateOn()) {
//            getCombat().toggleAutoRetaliate(false);
//            log("toggled auto retaliate");
//        }
//
//        getWine();
//        log("stole wine");
//
//
//    }
//
//    private void healing() {
//        while (getSkills().getDynamic(Skill.HITPOINTS) < 20 && getInventory().contains("Tuna")) {
//            getInventory().getItem("Tuna").interact("Eat");
//            Sleep.sleepUntil(random(500, 750));
//        }
//        if (!getInventory().contains("Tuna")) {
//            if (getWalking().webWalk(Banks.EDGEVILLE)) {
//                if (!getBank().isOpen()) {
//                    try {
//                        getBank().open();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                getBank().withdrawAll("Tuna");
//                Sleep.sleepUntil(random(500, 750));
//                getBank().close();
//                Sleep.sleepUntil(random(500, 750));
//                while (getSkills().getDynamic(Skill.HITPOINTS) < 20) {
//                    getInventory().getItem("Tuna").interact("Eat");
//                    Sleep.sleepUntil(random(750, 1000));
//                }
//                Sleep.sleepUntil(random(500, 750));
////                if (!getBank().isOpen()) {
////                    try {
////                        getBank().open();
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                }
////                if (getInventory().contains("Tuna")) getBank().depositAll("Tuna");
//            } else {
//                Sleep.sleepUntil(random(1500, 2000));
//                healing();
//            }
//        }
//    }
//
//    private void putOnEquipment() {
//        if (!getEquipment().contains("Zamorak monk top") && getInventory().contains("Zamorak monk top"))
//            getInventory().getItem("Zamorak monk top").interact("Wear");
//
//        Sleep.sleepUntil(random(500, 750));
//
//        if (!getEquipment().contains("Zamorak monk bottom") && getInventory().contains("Zamorak monk bottom"))
//            getInventory().getItem("Zamorak monk bottom").interact("Wear");
//
//        Sleep.sleepUntil(random(500, 750));
//
//        if (!getEquipment().contains("Staff of air") && getInventory().contains("Staff of air"))
//            getInventory().getItem("Staff of air").interact("Wield");
//
//        log("checked equipment");
//    }
//
//    private void moveCamera() {
//        GroundItem wine = getGroundItems().closest("Wine of zamorak");
//        getCamera().toEntity(wine);
//    }
//
//    private void chooseSpell() {
//        getMagic().castSpell(Spells.NormalSpells.TELEKINETIC_GRAB);
//    }
//
//    private void getToChaosTemple() {
//        Sleep.sleepUntil(random(500, 750));
//
//        if (!wineFirst.contains(myPosition())) {
//            getWalking().webWalk(wineFirst);
//            Sleep.sleepUntil(random(500, 750));
//        }
//        log("arrived to chaos temple");
//    }
//
//    private void targetWine() {
//        new ConditionalSleep(10000, 500) {
//            @Override
//            public boolean condition() throws InterruptedException {
//                log("sleep-targeting wine");
//                GroundItem wine = getGroundItems().closest("Wine of zamorak");
//                moveCamera();
//                return (wine != null && wine.isVisible());
//            }
//        }.sleep();
//        log("wine targeted");
//    }
//
//    private void getWine() {
//        while (!getInventory().isFull()) {
//
//            if (getSkills().getDynamic(Skill.HITPOINTS) < 15) {
//                healing();
//                log("healed");
//            }
//
//            getToChaosTemple();
//
//            targetWine();
//
//            chooseSpell();
//
//            stealWine();
//            iteration = 1;
//
//            goToSafeSpot();
//
//            hoppingWorld();
//        }
//
//        if (getInventory().isFull()) depositAll();
//    }
//
//    private void hoppingWorld() {
//        new ConditionalSleep(100000, 500) {
//            @Override
//            public boolean condition() throws InterruptedException {
//                return getWorlds().hopToP2PWorld();
//            }
//        }.sleep();
//        Sleep.sleepUntil(random(1000, 1200));
//    }
//
//
//    private void interactWidget() {
//        new ConditionalSleep(15000, 300) {
//            @Override
//            public boolean condition() throws InterruptedException {
//                RS2Widget stealWineWidget = getWidgets().get(219, 1, 1);
//                return (stealWineWidget != null && stealWineWidget.isVisible());
//            }
//        }.sleep();
//
//        RS2Widget stealWineWidget = getWidgets().get(219, 1, 1);
//        if (stealWineWidget != null && stealWineWidget.isVisible()) {
//            stealWineWidget.interact();
//        }
//    }
//
//    private void stealWine() {
//        GroundItem wine = getGroundItems().closest("Wine of zamorak");
//        if (!getMagic().isSpellSelected()) {
//            chooseSpell();
//        }
//        if (wine != null) {
//            wine.interact("Cast");
//            log("yes");
//
//            new ConditionalSleep(15000, 500) {
//                @Override
//                public boolean condition() throws InterruptedException {
//                    GroundItem wine = getGroundItems().closest("Wine of zamorak");
//                    if (iteration == 1 && wine.isVisible() && wine != null) {
//                        new ConditionalSleep(15000, 500) {
//                            @Override
//                            public boolean condition() throws InterruptedException {
//                                GroundItem wine = getGroundItems().closest("Wine of zamorak");
//                                interactWidget();
//                                log("fuck yes");
//                                return (wine == null && !wine.isVisible());
//                            }
//                        }.sleep();
//                    }
//                    return (wine == null && !wine.isVisible());
//                }
//            }.sleep();
//
//
//        }
//
//
//        log("stole wine");
//    }
//
//    private void goToSafeSpot() {
////        if (!getWalking().webWalk(safeSpot)) {
////            Sleep.sleepUntil(random(750, 1200));
////            goToSafeSpot();
////        }
//
//        new ConditionalSleep(10000, 500) {
//            @Override
//            public boolean condition() throws InterruptedException {
//                getWalking().webWalk(safeSpot);
//                return (safeSpot.contains(myPosition()));
//            }
//        }.sleep();
//
//        log("arrived to safe spot");
//    }
//
//    private void depositAll() {
//        log("depositin all");
//        if (getWalking().webWalk(Banks.EDGEVILLE)) {
//            if (!getBank().isOpen()) {
//                try {
//                    getBank().open();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                getBank().depositAll();
//                Sleep.sleepUntil(random(300, 500));
//            }
//        } else depositAll();
//        log("deposited all");
//    }

    //---------------------------------------------------------------------------------------------------


    @Override
    public void execute(int type) {
        if (getBank().isOpen()) getBank().close();
        else if (getGrandExchange().isOpen()) getGrandExchange().close();

        checkEverything();

        stealWine1();
    }

    private void putOnEquipment() {
        if (!getEquipment().contains("Zamorak monk top") && getInventory().contains("Zamorak monk top"))
            getInventory().getItem("Zamorak monk top").interact("Wear");

        Sleep.sleepUntil(random(500, 750));

        if (!getEquipment().contains("Zamorak monk bottom") && getInventory().contains("Zamorak monk bottom"))
            getInventory().getItem("Zamorak monk bottom").interact("Wear");

        Sleep.sleepUntil(random(500, 750));

        if (!getEquipment().contains("Staff of air") && getInventory().contains("Staff of air"))
            getInventory().getItem("Staff of air").interact("Wield");

        log("checked equipment");
    }

    private void checkEverything() {
        putOnEquipment();

        Sleep.sleepUntil(random(500, 750));

        if (getCombat().isAutoRetaliateOn()) getCombat().toggleAutoRetaliate(false);
    }

    private void isBankOpened() {
        if (!getBank().isOpen()) {
            try {
                getBank().open();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void stealWine1() {
        while (getSkills().getDynamic(Skill.HITPOINTS) < 15 || getInventory().contains("Tuna") || (getInventory().contains("Restore potion(1)") || getInventory().contains("Restore potion(2)") || getInventory().contains("Restore potion(3)") || getInventory().contains("Restore potion(4)"))) {

            if (getInventory().contains("Vial")) getInventory().getItem("Vial").interact("Drop");

            if (inventory.isFull()) {
                if (getInventory().contains("Tuna")) getInventory().getItem("Tuna").interact("Eat");
                Sleep.sleepUntil(random(300, 500));
            }

            if (getSkills().getDynamic(Skill.HITPOINTS) < 16) {
                healing1();
                log("healed");
            }
            Sleep.sleepUntil(random(500, 750));

            Sleep.sleepUntil(this::getToChaosTemple1, 100000, 3000);
            log("arrived to chaos temple");

            Sleep.sleepUntil(this::moveCamera, 100000, 3000);
            log("moved camera");

            Sleep.sleepUntil(this::chooseSpell1, 10000, 2000);
            log("choose spell");

            Sleep.sleepUntil(this::telegrabWine, 12000, 3000);
            log("telegrabed wine");
            Sleep.sleepUntil(random(500, 750));

            Sleep.sleepUntil(this::goToSafeSpot1, 15000, 1500);
            log("safe spotted");

            if (getSkills().getDynamic(Skill.HITPOINTS) < 16) {
                healing1();
                log("healed");
            }
            Sleep.sleepUntil(random(500, 750));

            Sleep.sleepUntil(random(500, 750));

            Sleep.sleepUntil(() -> {
                getWorlds().hopToP2PWorld();
                return getGroundItems().closest("Wine of zamorak") == null;
            }, 50000, 3000);
            log("hopped world");

        }
        if (getInventory().isFull()) depositAll();
    }

    private boolean moveCamera() {
        GroundItem wine = getGroundItems().closest("Wine of zamorak");
        if (!wine.isVisible()) {
            getCamera().toEntity(wine);
            return false;
        } else return true;
    }

    private void depositAll() {
        log("depositin all");
        if (getWalking().webWalk(Banks.EDGEVILLE)) {
            if (!getBank().isOpen()) {
                try {
                    getBank().open();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getBank().depositAll();
                Sleep.sleepUntil(random(300, 500));
            }
        } else depositAll();
        log("deposited all");
    }

    private boolean worldSwapping() {
        if (world != getWorlds().getCurrentWorld()) {
            return true;
        } else return false;
    }

    private boolean goToSafeSpot1() {
        log("safespotting");
        if (!safeSpot.contains(myPosition())) {
            getWalking().webWalk(safeSpot);
            return false;
        }
        return true;
    }

    private boolean telegrabWine() {
        log("telegrabing wine");
        GroundItem wine = getGroundItems().closest("Wine of zamorak");
        if (wine != null) {
            if (!wine.isVisible()) {
                getCamera().toEntity(wine);
                Sleep.sleepUntil(random(500, 750));
            }
            wine.interact("Cast");
            Sleep.sleepUntil(random(500, 750));
            while (getDialogues().inDialogue()) {
                if (getDialogues().isPendingOption()) {
                    getDialogues().selectOption("I'll take it anyway");
                } else getDialogues().clickContinue();
            }
        }

        GroundItem wineCheck = getGroundItems().closest("Wine of zamorak");
        if (wineCheck == null) {
            log("wino net");
        } else log("wina est");
        return wineCheck == null;
    }

    private boolean chooseSpell1() {
        log("choosing spell");
        try {
            if (!getMagic().canCast(Spells.NormalSpells.TELEKINETIC_GRAB)) {
                if (getTabs().isOpen(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);
                for (int i = 0; i < potions.length; i++) {
                    if (getInventory().contains(potions[i])) getInventory().getItem(potions[i]).interact("Drink");
                    Sleep.sleepUntil(random(500, 750));
                }
                return false;
            } else getMagic().castSpell(Spells.NormalSpells.TELEKINETIC_GRAB);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }


//    private boolean targetWine1() {
//        log("targeting wine");
//        GroundItem wine = getGroundItems().closest("Wine of zamorak");
//        if (wine != null) {
//            if (!wine.isVisible()) moveCamera();
//            return false;
//        }
//        return true;
//    }

    private boolean getToChaosTemple1() {
        log("webwalking to chaos temple");
        if (!wineFirst.contains(myPosition())) {
            getWalking().webWalk(wineFirst);
            return false;
        }
        return true;
    }


    private void healing1() {
        log("healing");
        if (!getInventory().contains("Tuna")) {
            if (getWalking().webWalk(Banks.FALADOR_WEST)) {
                isBankOpened();
                if (getBank().contains("Tuna")) getBank().withdraw("Tuna", 12);
                Sleep.sleepUntil(random(300, 500));
                getBank().close();
                Sleep.sleepUntil(random(300, 500));
                while (getSkills().getDynamic(Skill.HITPOINTS) < 20) {
                    if (getInventory().contains("Tuna")) getInventory().getItem("Tuna").interact("Eat");
                    Sleep.sleepUntil(random(300, 500));
                }
            }
        } else {
            while (getSkills().getDynamic(Skill.HITPOINTS) < 20) {
                getInventory().getItem("Tuna").interact("Eat");
                Sleep.sleepUntil(random(500, 750));
            }
        }
    }

}











