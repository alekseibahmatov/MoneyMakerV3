package activities.FillingBullseyeLantern;

import activities.setup.Task;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;

public class PlayerHasHouse extends Task {
    @Override
    public boolean isNeededToStartAtGE() {
        return true;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);

        return getInventory().contains("Bullseye lantern (empty)") &&
                getInventory().contains("Swamp tar") &&
                getInventory().contains("Teleport to house") &&
                (getInventory().contains("Ring of wealth (5)") ||
                        getInventory().contains("Ring of wealth (4)") ||
                        getInventory().contains("Ring of wealth (3)") ||
                        getInventory().contains("Ring of wealth (2)") ||
                        getInventory().contains("Ring of wealth (1)"));
    }

    private Area chemistArea = new Area(2930, 3213, 2936, 3208);

    private Area grandExchangeArea = new Area(3169, 3488, 3161, 3486);

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

        if (getInventory().contains("Teleport to house")) getInventory().getItem("Teleport to house").interact();
        log("activated 'Teleport to house'");

        log("teleporting to house");

        new ConditionalSleep(60000, 2000) {
            @Override
            public boolean condition() throws InterruptedException {
                log("sleep until see portal");
                RS2Object portal = getObjects().closest("Portal");
                return (portal != null && portal.isVisible());
            }
        }.sleep();

        RS2Object portal = getObjects().closest("Portal");

        if (portal != null && portal.isVisible()) {
            Sleep.sleepUntil(random(300, 500));
            portal.interact("Enter");
        }

        log("teleporting to chemist area");

        Sleep.sleepUntil(random(1500, 1750));

        if (!chemistArea.contains(myPosition())) getWalking().webWalk(chemistArea);

        log("walking to chemist");

        new ConditionalSleep(100000, 2000) {
            @Override
            public boolean condition() throws InterruptedException {
                log("sleep while walking in chemist house");
                return chemistArea.contains(myPosition());
            }
        }.sleep();

        log("in chemist house");

        new ConditionalSleep(60000, 1000) {
            @Override
            public boolean condition() {
                NPC chemist = getNpcs().closest("Chemist");
                return (chemist != null && chemist.isVisible());
            }
        }.sleep();

        log("chemist is visible");

        NPC chemist = getNpcs().closest("Chemist");

        if (chemist != null && chemist.isVisible()) {

            Sleep.sleepUntil(random(300, 500));

            getCamera().toEntity(chemist);

            Sleep.sleepUntil(random(300, 500));

            new ConditionalSleep(60000, 2000) {
                @Override
                public boolean condition() {
                    RS2Widget chemistDialogWidget = getWidgets().get(219, 1, 1);
                    NPC chemist = getNpcs().closest("Chemist");
                    chemist.interact();
                    return chemistDialogWidget != null;
                }
            }.sleep();
            log("interaction with chemist");
        }

        log("started dialog with chemist");

        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase0 = getWidgets().get(219, 1, 1);
                return (phrase0 != null && phrase0.isVisible());
            }
        }.sleep();
        RS2Widget phrase0 = getWidgets().get(219, 1, 1);
        phrase0.interact();
        log("Yes");


        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase1 = getWidgets().get(217, 3);
                return (phrase1 != null && phrase1.isVisible());
            }
        }.sleep();
        RS2Widget phrase1 = getWidgets().get(217, 3);
        phrase1.interact();
        log("Continue");


        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase2 = getWidgets().get(231, 3);
                return (phrase2 != null && phrase2.isVisible());
            }
        }.sleep();
        RS2Widget phrase2 = getWidgets().get(231, 3);
        phrase2.interact();
        log("Continue");


        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase3 = getWidgets().get(219, 1, 2);
                return (phrase3 != null && phrase3.isVisible());
            }
        }.sleep();
        RS2Widget phrase3 = getWidgets().get(219, 1, 2);
        phrase3.interact();
        log("No");


        new ConditionalSleep(5000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget phrase4 = getWidgets().get(217, 3);
                return (phrase4 != null && phrase4.isVisible());
            }
        }.sleep();
        RS2Widget phrase4 = getWidgets().get(217, 3);
        phrase4.interact();
        log("Finish dialog");

        Sleep.sleepUntil(random(300, 700));

        new ConditionalSleep(7000, 3000) {
            @Override
            public boolean condition() throws InterruptedException {
                log("sleep until see lamp oil still");
                RS2Object lampOilStill = getObjects().closest("Lamp oil still");
                getCamera().toEntity(lampOilStill);
                return (lampOilStill != null && lampOilStill.isVisible());
            }
        }.sleep();
        log("lamp oil still targeted");

        RS2Object lampOilStill = getObjects().closest("Lamp oil still");

        if (lampOilStill != null && lampOilStill.isVisible()) {
            while (getInventory().contains("Bullseye lantern (empty)") && getInventory().contains("Swamp tar")) {
                Sleep.sleepUntil(random(500, 750));
                if (getInventory().contains("Swamp tar")) getInventory().getItem("Swamp tar").interact();
                Sleep.sleepUntil(random(500, 750));
                lampOilStill.interact();
                Sleep.sleepUntil(random(500, 750));
                if (getInventory().contains("Bullseye lantern (empty)")) {
                    getInventory().getItem("Bullseye lantern (empty)").interact();
                }
                Sleep.sleepUntil(random(500, 750));
                lampOilStill.interact();
            }
        }
        log("interactions between lamp oil and bullseye lantern finished");

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

        Sleep.sleepUntil(random(1000, 1250));


        new ConditionalSleep(60000, 1000) {
            @Override
            public boolean condition() {
                if (!grandExchangeArea.contains(myPosition())) {
                    getWalking().webWalk(grandExchangeArea);
                }
                return grandExchangeArea.contains(myPosition());
            }
        }.sleep();
    }
}
