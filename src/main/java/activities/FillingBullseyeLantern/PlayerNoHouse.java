package activities.FillingBullseyeLantern;

import activities.setup.Task;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;

public class PlayerNoHouse extends Task {
    @Override
    public boolean isNeededToStartAtGE() {
        return true;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);
        log("benis has not hosue");

        return getInventory().contains("Falador teleport") &&
                (getInventory().contains("Ring of wealth (5)") ||
                        getInventory().contains("Ring of wealth (4)") ||
                        getInventory().contains("Ring of wealth (3)") ||
                        getInventory().contains("Ring of wealth (2)") ||
                        getInventory().contains("Ring of wealth (1)"));
    }

    private Area estateAgentArea = new Area(2981, 3370, 2984, 3369);

    @Override
    public void execute(int type) {
        if (getBank().isOpen()) getBank().close();
        else if (getGrandExchange().isOpen()) getGrandExchange().close();

        new ConditionalSleep(60000, 250) {
            @Override
            public boolean condition() throws InterruptedException {
                return !getBank().isOpen();
            }
        }.sleep();

        log("bank closed");

        if (getInventory().contains("Falador teleport")) getInventory().getItem("Falador teleport").interact();
        log("activated 'Falador teleport'");
        log("teleporting to Falador");

        new ConditionalSleep(60000, 2000) {
            @Override
            public boolean condition() throws InterruptedException {
                log("sleep until see statue");
                RS2Object statue = getObjects().closest("Statue of Saradomin");
                return (statue != null && statue.isVisible());
            }
        }.sleep();

        if (!estateAgentArea.contains(myPosition())) getWalking().webWalk(estateAgentArea);

        log("walking to estate agent");

        new ConditionalSleep(100000, 2000) {
            @Override
            public boolean condition() throws InterruptedException {
                log("sleep while walking in estate agent house");
                return estateAgentArea.contains(myPosition());
            }
        }.sleep();

        log("in estate agent house");

        new ConditionalSleep(60000, 1000) {
            @Override
            public boolean condition() {
                NPC estateAgent = getNpcs().closest("Estate agent");
                return (estateAgent != null && estateAgent.isVisible());
            }
        }.sleep();

        log("estate agent is visible");

        NPC estateAgent = getNpcs().closest("Estate agent");

        if (estateAgent != null && estateAgent.isVisible()) {

            Sleep.sleepUntil(random(300, 500));

            getCamera().toEntity(estateAgent);

            Sleep.sleepUntil(random(300, 500));

            new ConditionalSleep(60000, 2000) {
                @Override
                public boolean condition() {
                    RS2Widget estateAgentWidget = getWidgets().get(219, 1, 1);
                    NPC estateAgent = getNpcs().closest("Estate agent");
                    estateAgent.interact();
                    return estateAgentWidget != null;
                }
            }.sleep();
            log("interaction with estate agent");
        }

        log("started dialog with estate agent");

        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase0 = getWidgets().get(231, 3);
                return (phrase0 != null && phrase0.isVisible());
            }
        }.sleep();
        RS2Widget phrase0 = getWidgets().get(231, 3);
        phrase0.interact();
        log("start dialog");

        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase1 = getWidgets().get(219, 1, 1);
                return (phrase1 != null && phrase1.isVisible());
            }
        }.sleep();
        RS2Widget phrase1 = getWidgets().get(219, 1, 1);
        phrase1.interact();
        log("How can I get a house");

        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase2 = getWidgets().get(217, 3);
                return (phrase2 != null && phrase2.isVisible());
            }
        }.sleep();
        RS2Widget phrase2 = getWidgets().get(217, 3);
        phrase2.interact();
        log("Continue");

        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase3 = getWidgets().get(231, 3);
                return (phrase3 != null && phrase3.isVisible());
            }
        }.sleep();
        RS2Widget phrase3 = getWidgets().get(231, 3);
        phrase0.interact();
        log("Continue");

        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase4 = getWidgets().get(231, 3);
                return (phrase4 != null && phrase4.isVisible());
            }
        }.sleep();
        RS2Widget phrase4 = getWidgets().get(231, 3);
        phrase4.interact();
        log("Continue");

        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase5 = getWidgets().get(219, 1, 1);
                return (phrase5 != null && phrase5.isVisible());
            }
        }.sleep();
        RS2Widget phrase5 = getWidgets().get(219, 1, 1);
        phrase4.interact();
        log("Yes");

        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase6 = getWidgets().get(217, 3);
                return (phrase6 != null && phrase6.isVisible());
            }
        }.sleep();
        RS2Widget phrase6 = getWidgets().get(217, 3);
        phrase6.interact();
        log("Continue");

        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase7 = getWidgets().get(231, 3);
                return (phrase7 != null && phrase7.isVisible());
            }
        }.sleep();
        RS2Widget phrase7 = getWidgets().get(231, 3);
        phrase7.interact();
        log("Continue");

        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase8 = getWidgets().get(231, 3);
                return (phrase8 != null && phrase8.isVisible());
            }
        }.sleep();
        RS2Widget phrase8 = getWidgets().get(231, 3);
        phrase8.interact();
        log("Finish dialog");

        if (getInventory().contains("Ring of wealth (5)")) {
            getInventory().getItem("Ring of wealth (5)").interact("Rub");
        } else if (getInventory().contains("Ring of wealth (4)")) {
            getInventory().getItem("Ring of wealth (4)").interact("Rub");
        } else if (getInventory().contains("Ring of wealth (3)")) {
            getInventory().getItem("Ring of wealth (3)").interact("Rub");
        } else if (getInventory().contains("Ring of wealth (2)")) {
            getInventory().getItem("Ring of wealth (2)").interact("Rub");
        } else if (getInventory().contains("Ring of wealth (1)")) {
            getInventory().getItem("Ring of wealth (1)").interact("Rub");
        }
        Sleep.sleepUntil(random(500, 750));


        new ConditionalSleep(60000, 3000) {
            @Override
            public boolean condition() {
                RS2Widget teleportToGrandExchange = getWidgets().get(219, 1, 2);
                return (teleportToGrandExchange != null && teleportToGrandExchange.isVisible());
            }
        }.sleep();
        log("Teleporting to grand exchange");

        RS2Widget teleportToGrandExchange = getWidgets().get(219, 1, 2);
        teleportToGrandExchange.interact();

        Sleep.sleepUntil(random(1000, 1750));

    }
}
