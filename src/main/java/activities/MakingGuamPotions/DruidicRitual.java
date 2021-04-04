package activities.MakingGuamPotions;

import activities.setup.Task;
import org.osbot.rs07.api.Combat;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;

public class DruidicRitual extends Task {
    @Override
    public boolean isNeededToStartAtGE() {
        return false;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);
        log("ritual deda");

        return getInventory().contains("Raw bear meat") &&
                getInventory().contains("Raw rat meat") &&
                getInventory().contains("Raw beef") &&
                getInventory().contains("Raw chicken");
    }

    private Area stoneCircle = new Area(2920, 3488, 2928, 3480);

    private Area sanfewRoom = new Area(new Position(2897, 3431, 1), new Position(2900, 3426, 1));

    private Area ladder = new Area(2882, 3399, 2886, 3395);

    private Area ladderUnder = new Area(2881, 9800, 2886, 9795);

    private Area cauldronArea = new Area(2895, 9831, 2895, 9831);

    @Override
    public void execute(int type) {
        if (getBank().isOpen()) getBank().close();
        else if (getGrandExchange().isOpen()) getGrandExchange().close();

        log("searching for kaqemeex");
        findKaqemeex();

        log("started first dialog with kaqemeex");
        dialogKaqemeexFirst();

        log("searching for sanfew");
        findSanfew();

        log("started first dialog with kaqemeex");
        dialogSanfewFirst();

        log("going underground");
        goUnderground();

        log("boiling meat");
        boilMeat();

        log("going back");
        goUpside();

        log("searching for sanfew");
        findSanfew();

        log("started second dialog with kaqemeex");
        dialogSanfewSecond();

        log("searching for kaqemeex");
        findKaqemeex();

        log("started second dialog with kaqemeex");
        dialogKaqemeexSecond();

        log("finish");
        getWidgets().closeOpenInterface();

    }

    private void findKaqemeex() {
        while (!stoneCircle.contains(myPosition())) getWalking().webWalk(stoneCircle);

        new ConditionalSleep(60000, 1000) {
            @Override
            public boolean condition() {
                NPC kaqemeex = getNpcs().closest("Kaqemeex");
                log("targeting kaqemeex");
                getCamera().toEntity(kaqemeex);
                return (kaqemeex != null && kaqemeex.isVisible());
            }
        }.sleep();

        log("kaqemeex targeted");

        NPC kaqemeex = getNpcs().closest("Kaqemeex");

        if (kaqemeex != null && kaqemeex.isVisible()) {

            getCamera().toEntity(kaqemeex);

            Sleep.sleepUntil(random(300, 500));

            new ConditionalSleep(60000, 2000) {
                @Override
                public boolean condition() {
                    RS2Widget kaqemeexDialogWidget = getWidgets().get(217, 3);
                    NPC kaqemeex = getNpcs().closest("Kaqemeex");
                    kaqemeex.interact();
                    return kaqemeexDialogWidget != null;
                }
            }.sleep();
            log("interaction with kaqemeex");
        }
    }

    private void dialogKaqemeexFirst() {
        while (getDialogues().inDialogue()) {
            if (getDialogues().isPendingOption()) {
                getDialogues().selectOption("I'm in search of a quest.", "Okay, I will try and help.");
            } else getDialogues().clickContinue();
            Sleep.sleepUntil(random(1500, 3000));
        }
    }

    private void findSanfew() {
        while (!sanfewRoom.contains(myPosition())) getWalking().webWalk(sanfewRoom);
        NPC sanfew = getNpcs().closest("Sanfew");
        if (sanfew.isVisible() && sanfew != null) {
            getCamera().toEntity(sanfew);
            Sleep.sleepUntil(random(300, 500));
            sanfew.interact();
            while (getDialogues().inDialogue() == false) sanfew.interact();
        }
        Sleep.sleepUntil(random(300, 500));
    }

    private void dialogSanfewFirst() {
        while (getDialogues().inDialogue()) {
            if (getDialogues().isPendingOption()) {
                getDialogues().selectOption("I've been sent to help purify the Varrock stone circle.", "Ok, I'll do that then.", "I'll get on with it.");
            } else getDialogues().clickContinue();
            Sleep.sleepUntil(random(1500, 3000));
        }
    }

    private void goUnderground() {
        while (!ladder.contains(myPosition())) getWalking().webWalk(ladder);
        RS2Object ladderObject = getObjects().closest("Ladder");
        if (ladderObject != null && ladderObject.isVisible()) {
            while (!ladderUnder.contains(myPosition())) {
                ladderObject.interact();
            }
        }
        Sleep.sleepUntil(random(300, 500));
    }

    private void boilMeat() {
        getCombat().toggleAutoRetaliate(false);

        while (!cauldronArea.contains(myPosition())) {
            getWalking().webWalk(cauldronArea);
            if (getObjects().closest("Prison door") != null) {
                getObjects().closest("Prison door").interact();
            }
        }

        RS2Object cauldron = getObjects().closest("Cauldron of Thunder");

        if (getInventory().contains("Raw bear meat")) getInventory().getItem("Raw bear meat").interact();
        Sleep.sleepUntil(random(300, 500));
        cauldron.interact();
        Sleep.sleepUntil(random(300, 500));

        if (getInventory().contains("Raw rat meat")) getInventory().getItem("Raw rat meat").interact();
        Sleep.sleepUntil(random(300, 500));
        cauldron.interact();
        Sleep.sleepUntil(random(300, 500));

        if (getInventory().contains("Raw beef")) getInventory().getItem("Raw beef").interact();
        Sleep.sleepUntil(random(300, 500));
        cauldron.interact();
        Sleep.sleepUntil(random(300, 500));

        if (getInventory().contains("Raw chicken")) getInventory().getItem("Raw chicken").interact();
        Sleep.sleepUntil(random(300, 500));
        cauldron.interact();
        Sleep.sleepUntil(random(300, 500));
    }

    private void goUpside() {
        while (!ladderUnder.contains(myPosition())) getWalking().webWalk(ladderUnder);
        RS2Object ladderObject = getObjects().closest("Ladder");
        if (ladderObject != null && ladderObject.isVisible()) {
            while (!ladder.contains(myPosition())) {
                ladderObject.interact();
            }
        }
        Sleep.sleepUntil(random(300, 500));
    }

    private void dialogSanfewSecond() {
        while (getDialogues().inDialogue()) {
            getDialogues().clickContinue();
            Sleep.sleepUntil(random(1500, 3000));
        }
    }

    private void dialogKaqemeexSecond() {
        while (getDialogues().inDialogue()) {
            getDialogues().clickContinue();
            Sleep.sleepUntil(random(1500, 3000));
        }
    }
}


